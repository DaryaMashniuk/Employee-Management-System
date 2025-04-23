package by.mashnyuk.webapplication.pool;

import by.mashnyuk.webapplication.dbConfig.DbConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int POOL_SIZE = 8;

    private final BlockingQueue<ProxyConnection> freeConnections;
    private final BlockingQueue<ProxyConnection> usedConnections;
    private final AtomicBoolean isPoolInitialized = new AtomicBoolean(false);
    private final Lock poolLock = new ReentrantLock();
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("✅ MySQL driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            LOGGER.error("❌ Failed to load driver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }
    private static final ConnectionPool instance = new ConnectionPool();


    private ConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(POOL_SIZE);
        usedConnections = new LinkedBlockingQueue<>(POOL_SIZE);
        try {
            initialize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionPool getInstance() {
        return instance;
    }

    public void initialize() throws SQLException {
        if (isPoolInitialized.get()) {
            return;
        }

        poolLock.lock();
        try {
            if (isPoolInitialized.get()) {
                return;
            }

            for (int i = 0; i < POOL_SIZE; i++) {
                Connection realConnection = DriverManager.getConnection(
                        DbConfig.getJdbcUrl(),
                        DbConfig.getJdbcUser(),
                        DbConfig.getJdbcPassword()
                );
                ProxyConnection proxyConnection = new ProxyConnection(realConnection);
                freeConnections.add(proxyConnection);
                LOGGER.info("🔗 Connection {} created and added to pool", i + 1);
            }

            isPoolInitialized.set(true);
            LOGGER.info("✅ Connection pool initialized. Total connections: {}", POOL_SIZE);
        } finally {
            poolLock.unlock();
        }
    }

    public ProxyConnection getConnection() {
        if (!isPoolInitialized.get()) {
            throw new IllegalStateException("Connection pool is not initialized");
        }
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            if (!connection.isValid(1)) {
                LOGGER.warn("⚠ Connection is invalid, creating new one...");
                connection.reallyClose();
                connection = createNewConnection();
            }

            usedConnections.put(connection);
            LOGGER.debug("✅ Connection obtained. Free: {}, Used: {}", freeConnections.size(), usedConnections.size());
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("⛔ Thread interrupted while waiting for connection");
            throw new RuntimeException("Thread interrupted while waiting for connection", e);
        } catch (SQLException e) {
            LOGGER.error("⛔ Error getting valid connection", e);
            throw new RuntimeException("Error getting valid connection", e);
        }
    }

    public void releaseConnection(ProxyConnection connection) {
        if (connection == null) {
            LOGGER.warn("⚠ Attempt to release null connection!");
            return;
        }

        try {
            if (usedConnections.remove(connection)) {
                if (!connection.isClosed()) {
                    connection.setAutoCommit(true); // Сброс состояния соединения
                    connection.clearWarnings();
                    freeConnections.put(connection);
                    LOGGER.debug("🔄 Connection released to pool. Free: {}, Used: {}",
                            freeConnections.size(), usedConnections.size());
                } else {
                    LOGGER.warn("⚠ Connection was closed, replacing with new one");
                    freeConnections.put(createNewConnection());
                }
            } else {
                LOGGER.warn("⚠ Attempt to release a connection not from this pool!");
                connection.reallyClose();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("⛔ Thread interrupted while releasing connection");
        } catch (SQLException e) {
            LOGGER.error("⛔ Error resetting connection state", e);
            try {
                freeConnections.put(createNewConnection());
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                LOGGER.error("⛔ Thread interrupted while replacing broken connection");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void shutdown() {
        poolLock.lock();
        try {
            if (!isPoolInitialized.get()) {
                return;
            }

            LOGGER.info("🔴 Closing all connections...");
            closeConnections(freeConnections);
            closeConnections(usedConnections);

            freeConnections.clear();
            usedConnections.clear();
            isPoolInitialized.set(false);
            LOGGER.info("✅ Connection pool shutdown complete.");
        } finally {
            poolLock.unlock();
        }
    }

    private ProxyConnection createNewConnection() throws SQLException {
        Connection realConnection = DriverManager.getConnection(
                DbConfig.getJdbcUrl(),
                DbConfig.getJdbcUser(),
                DbConfig.getJdbcPassword()
        );
        return new ProxyConnection(realConnection);
    }

    private void closeConnections(BlockingQueue<ProxyConnection> connections) {
        for (ProxyConnection connection : connections) {
                connection.reallyClose();
        }
    }
}
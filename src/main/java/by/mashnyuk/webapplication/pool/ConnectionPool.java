package by.mashnyuk.webapplication.pool;

import by.mashnyuk.webapplication.dbConfig.DbConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int POOL_SIZE = 8;
    private final BlockingQueue<Connection> freeConnections = new LinkedBlockingQueue<>();
    private final BlockingQueue<Connection> usedConnections = new LinkedBlockingQueue<>();
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("✅ MySQL driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            LOGGER.error("❌ Failed driver: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    private static final ConnectionPool instance = new ConnectionPool();
    private ConnectionPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                Connection connection = DriverManager.getConnection(
                        DbConfig.getJdbcUrl(),
                        DbConfig.getJdbcUser(),
                        DbConfig.getJdbcPassword()
                );
                freeConnections.add(connection);
                LOGGER.info("🔗 Connection {} created and added to pool", i + 1);
            } catch (SQLException e) {
                LOGGER.error("❌ Error creating connection: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to initialize connection pool", e);
            }

        }
        LOGGER.info("✅ Connection pool initialized. Total connections: {}", freeConnections.size());
    }


    public static ConnectionPool getInstance() {
        return instance;
    }

    public Connection getConnection() {
        LOGGER.info("🔄 Getting connection from pool...");
        Connection connection = null;
        try {
            connection = freeConnections.poll(5, TimeUnit.SECONDS);
            if (connection == null) {
                LOGGER.error("⛔ Failed to get connection! All connections are busy.");
                throw new RuntimeException("All connections are busy. Try again later.");
            }
            usedConnections.put(connection);
            LOGGER.info("✅ Connection obtained. Free: {}, Used: {}", freeConnections.size(), usedConnections.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("⛔ Error getting connection: {}", e.getMessage(), e);
            throw new RuntimeException("Error getting connection", e);
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection == null) {
            LOGGER.warn("⚠ Attempt to release null connection!");
            return;
        }

        try {
            if (usedConnections.remove(connection)) {
                freeConnections.put(connection);
                LOGGER.info("🔄 Connection released to pool. Free: {}, Used: {}", freeConnections.size(), usedConnections.size());
            } else {
                LOGGER.warn("⚠ Attempt to release a connection not in usedConnections!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("⛔ Error releasing connection to pool", e);
        }
    }

    public void closeAllConnections() {
        LOGGER.info("🔴 Closing all connections...");
        for (Connection connection : freeConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("❌ Error closing connection: {}", e.getMessage(), e);
            }
        }
        for (Connection connection : usedConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("❌ Error closing connection: {}", e.getMessage(), e);
            }
        }
        freeConnections.clear();
        usedConnections.clear();
        LOGGER.info("✅ All connections closed.");
    }
}
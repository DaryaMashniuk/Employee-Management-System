
package by.mashnyuk.webapplication.dbConfig;

public class DbConfig {
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/employees?connectTimeout=10000&socketTimeout=20000";
    public static final String JDBC_USER = "root";
    public static final String JDBC_PASSWORD = System.getenv("PASSWORD");;

    public static String getJdbcUrl() {
        return JDBC_URL;
    }

    public static String getJdbcUser() {
        return JDBC_USER;
    }

    public static String getJdbcPassword() {
        return JDBC_PASSWORD;
    }
}

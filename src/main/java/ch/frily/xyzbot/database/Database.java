package ch.frily.xyzbot.database;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private static Database instance;

    // init database constants
    private static final String DATABASE_DRIVER = "org.postgresql.Driver";
    private static final String DATABASE_URL = EnvResolver.getString(EnvKey.CRED_DB_URL);
    private static final String USERNAME = EnvResolver.getString(EnvKey.CRED_DB_USERNAME);
    private static final String PASSWORD = EnvResolver.getString(EnvKey.CRED_DB_PASSWORD);

    // init connection object
    private Connection connection;
    // init properties object
    private Properties properties;

    public static Database getInstance(){
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Get the connection properties
     * @return The connection properties
     */
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
        }
        return properties;
    }

    /**
     * Connect to the database
     * @return The database connection
     */
    public Connection connect() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            Class.forName(DATABASE_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, getProperties());
        }
        return connection;
    }

    /**
     * Disconnect the current database connection
     */
    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }


}
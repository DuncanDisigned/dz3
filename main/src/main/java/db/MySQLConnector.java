package db;



import settings.PropertiesReader;

import java.sql.*;
import java.util.Map;

public class MySQLConnector implements IDBConnector {
    private static Connection connection = null;
    private static Statement statement = null;

    public MySQLConnector() throws SQLException {
        connect();
    }

    private void connect() throws SQLException {
        PropertiesReader reader = new PropertiesReader(); // Убираем ненужное присваивание null
        Map<String, String> settings = reader.read();
        if (settings == null) {
            throw new SQLException("Error: Failed to read database settings.");
        }

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        String.format("%s/%s", settings.get("url"), settings.get("db_name")),
                        settings.get("username"),
                        settings.get("password"));
            } catch (SQLException e) {
                throw new SQLException("Error: Failed to connect to database.", e);
            }
        }

        if (statement == null) {
            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                throw new SQLException("Error: Failed to create statement.", e);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void executeRequest(String response) {
        try {
            statement.execute(response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeRequestWithAnswer(String response) {
        try {
            return statement.executeQuery(response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

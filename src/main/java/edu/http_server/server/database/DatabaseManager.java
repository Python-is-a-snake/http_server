package edu.http_server.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// todo: make class a Component and inject to Dao layer
public final class DatabaseManager {

    private static final String url = "jdbc:mariadb://localhost:3306/http_server";
    private static final String username = "root";
    private static final String password = "root";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

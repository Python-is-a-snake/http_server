package edu.http_server.server.database;

import edu.http_server.server.di.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public final class DatabaseManager {

    private static final String url = "jdbc:mariadb://localhost:3306/http_server";
    private static final String username = "root";
    private static final String password = "root";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

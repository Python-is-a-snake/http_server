package edu.http_server.server.database;

import edu.http_server.server.config.AppProperties;
import edu.http_server.server.di.Component;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public final class DatabaseManager {

    private final AppProperties properties;
    private static final String URL = "database.url";
    private static final String USERNAME = "database.username";
    private static final String PASSWORD = "database.password";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(properties.getProperty(URL), properties.getProperty(USERNAME), properties.getProperty(PASSWORD));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

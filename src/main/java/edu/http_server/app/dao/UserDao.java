package edu.http_server.app.dao;

import edu.http_server.app.model.entity.User;
import edu.http_server.server.database.DatabaseManager;
import edu.http_server.server.di.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * User Data Access Object.
 * Contains standard CRUD (Create, Read, Update, Delete) operations;
 */
public class UserDao {
    private static final String CREATE_SQL = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_SQL = ""; // todo: implement
    private static final String FIND_ALL_SQL = "SELECT id, name, age, email FROM users";
    private static final String UPDATE_SQL = "UPDATE users"; // todo: implement
    private static final String DELETE_SQL = "DELETE FROM users"; // todo: implement

//    private final DatabaseManager dbManager; todo

    public User save(User user) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setInt(2, user.getAge());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public List<User> findAll() {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // todo: extract to parseUser(resultSet) method
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
                user.setEmail(resultSet.getString("email"));

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public Optional<User> findById(long id) {
        // todo: implement
        return Optional.empty();
    }

    public User update(User user) {
        // todo: implement
        return user;
    }

    public boolean deleteById(long id) {
        // todo: implement
        return false;
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        userDao.findAll().forEach(System.out::println);
    }
}

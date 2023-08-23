package edu.http_server.app.dao;

import edu.http_server.app.model.entity.User;
import edu.http_server.server.database.DatabaseManager;
import edu.http_server.server.di.Component;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * User Data Access Object.
 * Contains standard CRUD (Create, Read, Update, Delete) operations;
 */
@Component
@RequiredArgsConstructor
public class UserDao {

    private final DatabaseManager dbManager;
    private static final String CREATE_SQL = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT id, name, age, email FROM users WHERE id=?";
    private static final String FIND_ALL_SQL = "SELECT id, name, age, email FROM users";
    private static final String UPDATE_SQL = "UPDATE users SET name=?, age=?, email=? WHERE id=?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id=?";

    public User save(User user) {
        try (Connection connection = dbManager.getConnection();
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

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = parseUser(resultSet);

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }


    public Optional<User> findById(long id) {
        try(Connection connection = dbManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                User user = parseUser(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public User update(User user) {
        try(Connection connection = dbManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);
        ) {
            statement.setString(1, user.getName());
            statement.setInt(2, user.getAge());
            statement.setString(3, user.getEmail());
            statement.setLong(4, user.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public boolean deleteById(long id) {
        try(Connection connection = dbManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setLong(1, id);
            int rowsUpdated = statement.executeUpdate();
            if(rowsUpdated > 0){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private User parseUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setAge(resultSet.getInt("age"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }

}

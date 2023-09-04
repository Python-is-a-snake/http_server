package edu.http_server.app.dao;

import edu.http_server.app.model.entity.Task;
import edu.http_server.app.model.entity.User;
import edu.http_server.server.database.DatabaseManager;
import edu.http_server.server.di.Component;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskDao {
    private final DatabaseManager databaseManager;

    private final String CREATE_SQL = """
            INSERT INTO (name, description, is_done, user_id) VALUES (?, ?, ?, ?);
            """;

    private final String FIND_BY_ID_SQL = """ 
            SELECT
              t.id task_id,
              t.name task_name,
              t.description task_description,
              t.is_done task_is_done,
              u.id user_id,
              u.name user_name,
              u.age user_age,
              u.email user_email
            FROM tasks t
            JOIN users u ON t.user_id = u.id
            WHERE t.id = ?;
            """;

    private final String FIND_ALL_SQL = """ 
            SELECT
              t.id task_id,
              t.name task_name,
              t.description task_description,
              t.is_done task_is_done,
              u.id user_id,
              u.name user_name,
              u.age user_age,
              u.email user_email
            FROM tasks t
            JOIN users u ON t.user_id = u.id
            """;
    private final String UPDATE_SQL = """
            UPDATE tasks 
            SET name=?, description=?, is_done=?, user_id=?
            WHERE id=?;
            """;

    private final String DELETE_BY_ID_SQL = """
            DELETE FROM tasks
            WHERE id=?
            """;

    public Task save(Task task) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setBoolean(3, task.isDone());
            preparedStatement.setLong(4, task.getUser().getId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                task.setId(generatedKeys.getLong("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    public Optional<Task> findById(long id) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Task task = parseTask(resultSet);
                return Optional.of(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<Task> findAll() {
        ArrayList<Task> tasks = new ArrayList<>();

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(parseTask(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    public Task update(Task task) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setBoolean(3, task.isDone());
            preparedStatement.setLong(4, task.getUser().getId());
            preparedStatement.setLong(5, task.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    public boolean deleteById(long id) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Task parseTask(ResultSet resultSet) throws SQLException {
        Task task = new Task();
        User user = new User();
        task.setId(resultSet.getLong("task_id"));
        task.setName(resultSet.getString("task_name"));
        task.setDescription(resultSet.getString("task_description"));
        task.setDone(resultSet.getBoolean("task_is_done"));

        user.setId(resultSet.getLong("user_id"));
        user.setName(resultSet.getString("user_name"));
        user.setAge(resultSet.getInt("user_age"));
        user.setEmail(resultSet.getString("user_email"));

        task.setUser(user);

        return task;
    }
}

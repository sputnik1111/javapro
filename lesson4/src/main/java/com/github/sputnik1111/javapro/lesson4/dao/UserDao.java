package com.github.sputnik1111.javapro.lesson4.dao;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(User user) {
        String sql = "INSERT INTO users (id,username) values (?,?)";
        execute(sql, preparedStatement -> {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            return preparedStatement.executeUpdate();
        });
    }

    public boolean update(Long userId, String userName) {
        String sql = "UPDATE users SET username = ? WHERE id = ? ";
        return execute(sql, preparedStatement -> {
            preparedStatement.setString(1, userName);
            preparedStatement.setLong(2, userId);
            return preparedStatement.executeUpdate() == 1;
        });
    }

    public boolean delete(Long userId) {
        String sql = "DELETE FROM users WHERE id = ? ";
        return execute(sql, preparedStatement -> {
            preparedStatement.setLong(1, userId);
            return preparedStatement.executeUpdate() == 1;
        });
    }

    public Optional<User> findById(Long userId) {
        String sql = "SELECT id,username FROM users WHERE id = ?";
        List<User> users = execute(sql, preparedStatement -> {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapResultSet(resultSet);
            }
        });

        return users.isEmpty()
                ? Optional.empty()
                : Optional.of(users.get(0));
    }

    public List<User> findAll() {
        String sql = "SELECT id,username FROM users";
        return execute(sql, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapResultSet(resultSet);
            }
        });
    }

    public void clear() {
        String sql = "TRUNCATE TABLE users";
        execute(sql, preparedStatement ->
                preparedStatement.executeUpdate() == 1
        );
    }

    private <T> T execute(
            String sql,
            StatementCallBack<T> callBack
    ) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            return callBack.execute(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> mapResultSet(ResultSet rs) throws SQLException {
        List<User> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new User(
                    rs.getLong("id"),
                    rs.getString("username")
            ));
        }
        return result;
    }

    @FunctionalInterface
    private interface StatementCallBack<T> {
        T execute(PreparedStatement preparedStatement) throws SQLException;
    }

}

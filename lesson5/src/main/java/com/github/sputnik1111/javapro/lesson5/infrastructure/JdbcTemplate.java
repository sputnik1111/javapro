package com.github.sputnik1111.javapro.lesson5.infrastructure;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T execute(
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


    @FunctionalInterface
    public interface StatementCallBack<T> {
        T execute(PreparedStatement preparedStatement) throws SQLException;
    }
}

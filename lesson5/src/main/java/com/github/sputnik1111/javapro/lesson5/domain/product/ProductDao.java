package com.github.sputnik1111.javapro.lesson5.domain.product;

import com.github.sputnik1111.javapro.lesson5.infrastructure.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {

    private final JdbcTemplate template;

    private static final String SELECT_ALL_PRODUCT =
            "SELECT id,user_id,account,balance,type_product FROM user_product";

    public ProductDao(JdbcTemplate template) {
        this.template = template;
    }

    public void insert(Product product) {
        String sql = "INSERT INTO user_product (id,user_id,account,balance,type_product) values (?,?,?,?,?)";
        template.execute(sql, preparedStatement -> {
            preparedStatement.setLong(1, product.getId());
            preparedStatement.setLong(2, product.getUserId());
            preparedStatement.setString(3, product.getAccount());
            preparedStatement.setLong(4, product.getBalance());
            preparedStatement.setString(5, product.getTypeProduct().toString());
            return preparedStatement.executeUpdate();
        });
    }

    public Optional<Product> findById(Long productId) {
        String sql = SELECT_ALL_PRODUCT + " WHERE id = ?";
        List<Product> users = template.execute(sql, preparedStatement -> {
            preparedStatement.setLong(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapResultSet(resultSet);
            }
        });

        return users.isEmpty()
                ? Optional.empty()
                : Optional.of(users.get(0));
    }

    public List<Product> findByUserId(Long userId) {
        String sql = SELECT_ALL_PRODUCT + " WHERE user_id = ?";
        return template.execute(sql, preparedStatement -> {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapResultSet(resultSet);
            }
        });
    }

    private List<Product> mapResultSet(ResultSet rs) throws SQLException {
        List<Product> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new Product(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("account"),
                    rs.getLong("balance"),
                    Enum.valueOf(Product.TypeProduct.class, rs.getString("type_product"))
            ));
        }
        return result;
    }
}

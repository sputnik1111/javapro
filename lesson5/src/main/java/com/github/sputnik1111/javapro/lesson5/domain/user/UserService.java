package com.github.sputnik1111.javapro.lesson5.domain.user;

import com.github.sputnik1111.javapro.lesson5.domain.product.Product;
import com.github.sputnik1111.javapro.lesson5.domain.product.ProductDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    private final ProductDao productDao;

    public UserService(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }

    public void insert(User user) {
        userDao.insert(user);
    }

    public boolean update(Long userId, String userName) {
        return userDao.update(userId, userName);
    }

    public boolean delete(Long userId) {
        return userDao.delete(userId);
    }

    public Optional<User> findById(Long userId) {
        return userDao.findById(userId);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void addProduct(Product product) {
        productDao.insert(product);
    }

    public List<Product> findByUserId(Long userId) {
        return productDao.findByUserId(userId);
    }

    public Optional<Product> findByProductId(Long productId) {
        return productDao.findById(productId);
    }

    public void clear() {
        userDao.clear();
    }
}

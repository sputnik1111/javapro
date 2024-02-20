package com.github.sputnik1111.javapro.lesson4.dao;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
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

    public void clear() {
        userDao.clear();
    }
}

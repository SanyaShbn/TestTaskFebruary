package com.example.testtaskfebruary.service;

import com.example.testtaskfebruary.dao.UserDao;
import com.example.testtaskfebruary.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional
    public void saveUser(User user) {
        userDao.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userDao.update(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userDao.delete(user);
    }

}

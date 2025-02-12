package com.example.testtaskfebruary.service;

import com.example.testtaskfebruary.dao.UserDao;
import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.User;
import com.example.testtaskfebruary.mapper.UserMapper;
import com.example.testtaskfebruary.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    private final UserMapper userMapper;

    public List<UserReadDto> getAllUsers() {
        return userDao.findAll().stream()
                .map(userMapper::userToUserReadDto)
                .collect(Collectors.toList());
    }

    public Optional<UserReadDto> findById(Long id) {
        return userDao.findById(id)
                .map(userMapper::userToUserReadDto);
    }

    public Optional<UserReadDto> findByEmail(String email) {
        return userDao.findByEmail(email)
                .map(userMapper::userToUserReadDto);
    }

    @Transactional
    public void saveUser(UserCreateEditDto userCreateEditDto) {
        User user = userMapper.userCreateEditDtoToUser(userCreateEditDto);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDao.save(user);
    }

    @Transactional
    public void updateUser(Long id, UserCreateEditDto userCreateEditDto) {
        User user = userMapper.userCreateEditDtoToUser(userCreateEditDto);
        user.setId(id);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDao.update(user);
    }

    @Transactional
    public boolean deleteUser(Long id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isPresent()) {
            userDao.delete(optionalUser.get());
            return true;
        }
        return false;
    }

}

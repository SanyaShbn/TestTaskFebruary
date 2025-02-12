package com.example.testtaskfebruary.service;

import com.example.testtaskfebruary.dao.UserDao;
import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.User;
import com.example.testtaskfebruary.exception.DaoException;
import com.example.testtaskfebruary.exception.EmailAlreadyExistsException;
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
        if (userDao.findByEmail(userCreateEditDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = userMapper.userCreateEditDtoToUser(userCreateEditDto);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDao.save(user);
    }

    @Transactional
    public void updateUser(Long id, UserCreateEditDto userCreateEditDto) {
        Optional<User> optionalUserFromDb = userDao.findById(id);
        if (optionalUserFromDb.isPresent()) {
            User existingUser = optionalUserFromDb.get();
            User user = userMapper.userCreateEditDtoToUser(userCreateEditDto);
            user.setId(id);

            if (userCreateEditDto.getPassword() == null || userCreateEditDto.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(PasswordUtil.hashPassword(userCreateEditDto.getPassword()));
            }

            userDao.update(user);
        } else {
            throw new DaoException("User not found with id: " + id);
        }
    }

    @Transactional
    public void updatePassword(Long id, String password) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(PasswordUtil.hashPassword(password));
            userDao.update(user);
        }
    }

}

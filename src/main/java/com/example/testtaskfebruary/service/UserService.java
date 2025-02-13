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

import java.util.Optional;

/**
 * Service class for handling user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    private final UserMapper userMapper;

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user, not null
     * @return an Optional containing the found {@link UserReadDto}, or an empty Optional if no user was found
     */
    public Optional<UserReadDto> findById(Long id) {
        return userDao.findById(id)
                .map(userMapper::userToUserReadDto);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for, not null
     * @return an Optional containing the found {@link UserReadDto}, or an empty Optional if no user was found
     */
    public Optional<UserReadDto> findByEmail(String email) {
        return userDao.findByEmail(email)
                .map(userMapper::userToUserReadDto);
    }

    /**
     * Saves a new user to the database.
     *
     * @param userCreateEditDto the DTO containing user information, not null
     * @throws EmailAlreadyExistsException if a user with the given email already exists
     */
    @Transactional
    public void saveUser(UserCreateEditDto userCreateEditDto) {
        if (userDao.findByEmail(userCreateEditDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = userMapper.userCreateEditDtoToUser(userCreateEditDto);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDao.save(user);
    }

    /**
     * Updates an existing user in the database.
     *
     * @param id the ID of the user to update, not null
     * @param userCreateEditDto the DTO containing updated user information, not null
     * @throws DaoException if the user is not found with the given ID
     */
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

    /**
     * Updates the password for an existing user.
     *
     * @param id the ID of the user, not null
     * @param password the new password to set, not null
     */
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

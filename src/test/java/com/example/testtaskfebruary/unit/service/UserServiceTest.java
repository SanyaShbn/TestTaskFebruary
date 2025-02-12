package com.example.testtaskfebruary.unit.service;

import com.example.testtaskfebruary.dao.UserDao;
import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.entity.User;
import com.example.testtaskfebruary.exception.DaoException;
import com.example.testtaskfebruary.exception.EmailAlreadyExistsException;
import com.example.testtaskfebruary.mapper.UserMapper;
import com.example.testtaskfebruary.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final Long USER_ID = 1L;

    @Mock
    private UserDao userDao;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testSaveUser_EmailAlreadyExists() {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder().email("test@example.com").build();

        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.saveUser(userCreateEditDto));

        verify(userDao).findByEmail(anyString());
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    public void testSaveUser_Success() {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder().email("test@example.com").password("password").build();
        User user = new User();

        when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userMapper.userCreateEditDtoToUser(any(UserCreateEditDto.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.saveUser(userCreateEditDto));

        verify(userDao).findByEmail(anyString());
        verify(userMapper).userCreateEditDtoToUser(any(UserCreateEditDto.class));
        verify(userDao).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder().email("test@example.com").build();

        when(userDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DaoException.class, () -> userService.updateUser(USER_ID, userCreateEditDto));

        verify(userDao).findById(anyLong());
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    public void testUpdateUser_Success() {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder().email("test@example.com").build();
        User existingUser = new User();
        existingUser.setPassword("existingPassword");
        User updatedUser = new User();

        when(userDao.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userMapper.userCreateEditDtoToUser(any(UserCreateEditDto.class))).thenReturn(updatedUser);

        assertDoesNotThrow(() -> userService.updateUser(USER_ID, userCreateEditDto));

        verify(userDao).findById(anyLong());
        verify(userMapper).userCreateEditDtoToUser(any(UserCreateEditDto.class));
        verify(userDao).update(any(User.class));
    }

    @Test
    public void testUpdatePassword_Success() {
        User user = new User();

        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.updatePassword(USER_ID, "newPassword"));

        verify(userDao).findById(anyLong());
        verify(userDao).update(any(User.class));
    }
}
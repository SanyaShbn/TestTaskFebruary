package com.example.testtaskfebruary.unit.dao;

import com.example.testtaskfebruary.dao.UserDao;
import com.example.testtaskfebruary.entity.User;
import com.example.testtaskfebruary.exception.DaoException;
import jakarta.persistence.EntityManager;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserDao userDao;

    @Test
    public void testSaveUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        doNothing().when(entityManager).persist(any(User.class));

        assertDoesNotThrow(() -> userDao.save(user));
        verify(entityManager).persist(any(User.class));
    }

    @Test
    public void testSaveUser_HibernateException() {
        User user = new User();
        user.setEmail("test@example.com");

        doThrow(new HibernateException("Error")).when(entityManager).persist(any(User.class));

        assertThrows(DaoException.class, () -> userDao.save(user));
        verify(entityManager).persist(any(User.class));
    }

    @Test
    public void testUpdateUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        when(entityManager.merge(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userDao.update(user));
        verify(entityManager).merge(any(User.class));
    }

    @Test
    public void testUpdateUser_HibernateException() {
        User user = new User();
        user.setEmail("test@example.com");

        when(entityManager.merge(any(User.class))).thenThrow(new HibernateException("Error"));

        assertThrows(DaoException.class, () -> userDao.update(user));
        verify(entityManager).merge(any(User.class));
    }
}
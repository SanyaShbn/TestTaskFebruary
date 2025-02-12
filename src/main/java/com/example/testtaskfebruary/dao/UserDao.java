package com.example.testtaskfebruary.dao;

import com.example.testtaskfebruary.entity.User;
import com.example.testtaskfebruary.exception.DaoException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserDao extends AbstractHibernateDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User findByEmail(String email) {
        try {
            String hql = "FROM User as usr WHERE usr.email = :email";
            User user = (User) entityManager.createQuery(hql)
                    .setParameter("email", email)
                    .getSingleResult();
            log.info("User found with email: {}", email);
            return user;
        } catch (HibernateException e) {
            log.error("Failed to find user by email: {}", email, e);
            throw new DaoException("Failed to find user by email", e);
        }
    }
}
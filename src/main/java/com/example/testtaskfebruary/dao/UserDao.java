package com.example.testtaskfebruary.dao;

import com.example.testtaskfebruary.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for the {@link User} entity.
 */
@Slf4j
@Repository
public class UserDao extends AbstractHibernateDao<User> {

    public UserDao() {
        super(User.class);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for, not null
     * @return an Optional containing the found {@link User}, or an empty Optional if no user was found
     */
    public Optional<User> findByEmail(String email) {
        try {
            String hql = "FROM User as usr WHERE usr.email = :email";
            List<User> users = entityManager.createQuery(hql)
                    .setParameter("email", email)
                    .getResultList();

            if (users.isEmpty()) {
                log.error("Failed to find user by email: {}", email);
                return Optional.empty();
            } else {
                User user = users.get(0);
                log.info("User found with email: {}", email);
                return Optional.of(user);
            }
        } catch (HibernateException e) {
            log.error("Failed to find user by email: {}", email, e);
            return Optional.empty();
        }
    }
}
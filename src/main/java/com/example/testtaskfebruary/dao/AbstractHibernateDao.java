package com.example.testtaskfebruary.dao;

import com.example.testtaskfebruary.exception.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractHibernateDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    public AbstractHibernateDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public List<T> findAll() {
        try {
            TypedQuery<T> query = entityManager
                    .createQuery("FROM " + entityClass.getName(), entityClass);
            return query.getResultList();
        } catch (HibernateException e) {
            log.error("Failed to find all entities", e);
            throw new DaoException("Failed to find all entities", e);
        }
    }

    public Optional<T> findById(Long id) {
        try {
            return Optional.ofNullable(entityManager.find(entityClass, id));
        } catch (HibernateException e) {
            log.error("Failed to find entity by ID: {}", id, e);
            throw new DaoException("Failed to find entity by ID", e);
        }
    }

    public void save(T entity) {
        try {
            entityManager.persist(entity);
            log.info("Entity {} successfully saved", entity);
        } catch (HibernateException e) {
            log.error("Failed to save entity {}", entity);
            throw new DaoException("Failed to save entity", e);
        }
    }

    public void update(T entity) {
        try {
            entityManager.merge(entity);
            log.info("Entity {} successfully updated", entity);
        } catch (HibernateException e) {
            log.error("Failed to update entity {}", entity);
            throw new DaoException("Failed to update entity", e);
        }
    }

    public void delete(T entity) {
        try {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            log.info("Entity {} successfully deleted", entity);
        } catch (HibernateException e) {
            log.error("Failed to delete entity {}", entity);
            throw new DaoException("Failed to delete entity", e);
        }
    }
}
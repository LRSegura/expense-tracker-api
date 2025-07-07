package com.dev2ever.repository;

import com.dev2ever.model.ErrorCode;
import com.dev2ever.model.User;
import com.dev2ever.util.OperationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class UserRepository {

    Logger logger = Logger.getLogger(UserRepository.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public OperationResult<User> save(User user) {
        try {
            entityManager.persist(user);
            return OperationResult.success(user);
        } catch (PersistenceException e) {
            logger.warning("Constraint violation while saving user: " + e.getMessage());
            return OperationResult.error(ErrorCode.DUPLICATE_RESOURCE, "Username or email already exists.");
        } catch (Exception e) {
            logger.severe("Error saving user: " + e.getMessage());
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred while saving the user.");
        }
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public Optional<User> findByUsername(String username) {
        String query = "SELECT u FROM User u WHERE u.username = :username";
        return entityManager.createQuery(query, User.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        String query = "SELECT u FROM User u WHERE u.email = :email";
        return entityManager.createQuery(query, User.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst();

    }

    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
package com.dev2ever.repository;

import com.dev2ever.api.Result;
import com.dev2ever.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.ArrayList;

@ApplicationScoped
public class UserRepository {

    Logger logger = Logger.getLogger(UserRepository.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Result<User> save(User user) {
        List<String> validationError = validateUser(user);
        if (!validationError.isEmpty()) {
            return Result.error(String.join(", ", validationError));
        }
        List<String> uniqueError = checkUniqueConstraints(user);
        if (!uniqueError.isEmpty()) {
            return Result.error(String.join(", ", uniqueError));
        }

        try {
            entityManager.persist(user);
            return Result.success(user);
        } catch (Exception e) {
            logger.severe("Error saving user: " + e.getMessage());
            return Result.error("Internal error saving user");
        }
    }

    private List<String> validateUser(User user) {
        List<String> errors = new ArrayList<>();
        if (user.getUsername() == null) {
            errors.add("Username cannot be null");
        }
        if (user.getEmail() == null) {
            errors.add("Email cannot be null");
        }
        if (user.getPassword() == null) {
            errors.add("Password cannot be null");
        }
        return errors;
    }

    private List<String> checkUniqueConstraints(User user) {
        List<String> errors = new ArrayList<>();
        if (findByUsername(user.getUsername()).isPresent()) {
            errors.add("Username already exists");
        }
        if (findByEmail(user.getEmail()).isPresent()) {
            errors.add("Email already exists");
        }
        return errors;
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
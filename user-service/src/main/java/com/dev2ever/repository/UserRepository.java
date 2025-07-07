package com.dev2ever.repository;

import com.dev2ever.model.ErrorCode;
import com.dev2ever.model.User;
import com.dev2ever.util.OperationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


/**
 * Repository class for managing User entities in the database.
 * Provides methods for CRUD operations and user lookups.
 * Uses JPA EntityManager for database operations.
 */
@ApplicationScoped
public class UserRepository {

    private final Logger logger = Logger.getLogger(UserRepository.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persists a new user to the database.
     *
     * @param user The user entity to be saved
     * @return OperationResult containing either the saved user or error details
     * if the operation failed due to constraints or other issues
     */
    @Transactional
    public OperationResult<User> save(User user) {
        try {
            entityManager.persist(user);
            entityManager.flush();
            return OperationResult.success(user);
        } catch (ConstraintViolationException e) {
            logger.severe("Constraint violation while saving user: " + e.getMessage());
            return OperationResult.error(ErrorCode.DUPLICATE_RESOURCE, "Username or email already exists.");
        } catch (Exception e) {
            logger.severe("Error saving user: " + e.getMessage());
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred while saving the user.");
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to find
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username to search for
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> findByUsername(String username) {
        String query = "SELECT u FROM User u WHERE u.username = :username";
        return entityManager.createQuery(query, User.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst();
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address to search for
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> findByEmail(String email) {
        String query = "SELECT u FROM User u WHERE u.email = :email";
        return entityManager.createQuery(query, User.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst();

    }

    /**
     * Retrieves all users from the database.
     *
     * @return List of all users in the database
     */
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    /**
     * Deletes a user from the database by their ID.
     *
     * @param id The ID of the user to delete
     * @return OperationResult indicating success or failure of the deletion
     */
    @Transactional
    public OperationResult<Void> deleteById(Long id) {
        try {
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
                return OperationResult.success();
            }
            return OperationResult.error(ErrorCode.NOT_FOUND, "User not found with ID: " + id);
        } catch (Exception e) {
            logger.severe("Error deleting user: " + e.getMessage());
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred while deleting the user.");
        }
    }

    /**
     * Updates the fields of an existing user in the database.
     *
     * @param id          The ID of the user to update
     * @param updatedUser The user object containing updated information
     * @return OperationResult containing the updated user or error details if the operation failed
     */
    @Transactional
    public OperationResult<User> updateUserFields(Long id, User updatedUser) {
        try {
            User existingUser = entityManager.find(User.class, id);
            if (existingUser == null) {
                return OperationResult.error(ErrorCode.NOT_FOUND, "User not found with ID: " + id);
            }

            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setFullName(updatedUser.getFullName());

            entityManager.flush();
            return OperationResult.success(existingUser);
        } catch (ConstraintViolationException e) {
            logger.severe("Constraint violation while updating user: " + e.getMessage());
            return OperationResult.error(ErrorCode.DUPLICATE_RESOURCE, "Username or email already exists.");
        } catch (Exception e) {
            logger.severe("Error updating user: " + e.getMessage());
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred while updating the user.");
        }
    }


}
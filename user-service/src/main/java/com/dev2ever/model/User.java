package com.dev2ever.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;


/**
 * Represents a user entity in the system.
 * This class contains user-related information such as username, email, password, and full name.
 * It is mapped to the "users" database table and includes validation constraints for its fields.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User implements Serializable {

    /**
     * The unique identifier for the user.
     * This ID is automatically generated using a sequence strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * The unique username of the user.
     * Must not be blank and must be unique across all users.
     */
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    /**
     * The email address of the user.
     * Must be a valid email format, not blank, and unique across all users.
     */
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The user's password.
     * Must not be blank and is stored in the database.
     */
    @NotBlank(message = "Password cannot be blank")
    @Column(nullable = false)
    private String password;

    /**
     * The full name of the user.
     * Must not be blank.
     */
    @NotBlank(message = "Full name cannot be blank")
    @Column
    private String fullName;

    /**
     * Checks if this user is equal to another object.
     * Two users are considered equal if they have the same non-null ID.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    /**
     * Generates a hash code for this user.
     * The hash code is based on the user's class to ensure consistency with equals method.
     *
     * @return the hash code value for this user
     */
    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}

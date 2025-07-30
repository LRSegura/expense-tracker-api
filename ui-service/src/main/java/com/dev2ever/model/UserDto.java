package com.dev2ever.model;

public record UserDto(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String password) {

    @Override
    public String toString() {
        return "UserDto{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

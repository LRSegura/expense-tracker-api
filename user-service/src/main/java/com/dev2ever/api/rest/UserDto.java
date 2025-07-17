package com.dev2ever.api.rest;

public record UserDto(
        String username,
        String email,
        String firstName,
        String lastName,
        String password) {

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

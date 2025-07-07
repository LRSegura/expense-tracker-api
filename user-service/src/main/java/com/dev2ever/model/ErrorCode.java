package com.dev2ever.model;


/**
 * Enumeration of error codes used throughout the application to identify different types of errors.
 * These codes are used in conjunction with error messages to provide structured error responses
 * in the API and service layers.
 */
public enum ErrorCode {
    /**
     * Indicates that field validation has failed during data processing.
     * Used when input data doesn't meet the required validation criteria.
     */
    FIELD_VALIDATION_ERROR,

    /**
     * Indicates an attempt to create a resource that already exists.
     * Used in scenarios such as unique constraint violations.
     */
    DUPLICATE_RESOURCE,

    /**
     * Indicates an unexpected error occurred during processing.
     * Used for system-level errors that are not handled by other specific error codes.
     */
    INTERNAL_SERVER_ERROR
}

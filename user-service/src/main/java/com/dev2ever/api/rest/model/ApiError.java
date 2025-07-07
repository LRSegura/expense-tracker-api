package com.dev2ever.api.rest.model;

import com.dev2ever.model.ErrorCode;


/**
 * Represents an API error response containing an error message and corresponding error code.
 * This record is used within {@link ApiResponse} to provide structured error information
 * when API operations fail.
 */
public record ApiError(
        String message,

        ErrorCode code) {
}

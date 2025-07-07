package com.dev2ever.api.exception;

import com.dev2ever.api.rest.model.ApiResponse;
import com.dev2ever.model.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;


/**
 * Exception mapper that handles {@link ConstraintViolationException} by converting validation errors
 * into a structured API response. This mapper is automatically discovered and registered through the
 * {@link Provider} annotation in the JAX-RS environment.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    /**
     * Converts a {@link ConstraintViolationException} into an HTTP response containing validation error details.
     *
     * @param exception The constraint violation exception to be handled
     * @return A Response object with HTTP 400 (Bad Request) status and validation error messages
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return ApiResponse.error(String.join(", ", errors), ErrorCode.FIELD_VALIDATION_ERROR)
                .buildBadRequestResponse();        
    }
}

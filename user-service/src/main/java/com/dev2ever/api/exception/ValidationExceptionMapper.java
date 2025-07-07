package com.dev2ever.api.exception;

import com.dev2ever.api.rest.model.ApiResponse;
import com.dev2ever.model.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        ApiResponse<Void> response = ApiResponse.error(String.join(", ", errors), ErrorCode.FIELD_VALIDATION_ERROR);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}

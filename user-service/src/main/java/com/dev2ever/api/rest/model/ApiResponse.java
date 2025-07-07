package com.dev2ever.api.rest.model;

import com.dev2ever.model.ErrorCode;
import jakarta.ws.rs.core.Response;
import lombok.Getter;


/**
 * A generic wrapper class for API responses that includes success status, data payload, and error information.
 * This class provides a standardized format for all API responses in the application.
 *
 * @param <T> The type of data payload that this response will contain
 */
@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ApiError error;

    /**
     * Private constructor to enforce the use of static factory methods.
     *
     * @param success indicates if the operation was successful
     * @param data    the payload of the response
     * @param error   error details if the operation failed
     */
    private ApiResponse(boolean success, T data, ApiError error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    /**
     * Creates a successful response without any data payload.
     *
     * @param <T> The type parameter for the response
     * @return A successful ApiResponse instance with null data
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, null);
    }

    /**
     * Creates a successful response with the provided data payload.
     *
     * @param <T>  The type parameter for the response
     * @param data The data to be included in the response
     * @return A successful ApiResponse instance containing the provided data
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    /**
     * Creates an error response with the specified message and error code.
     *
     * @param message The error message describing what went wrong
     * @param code    The specific error code indicating the type of error
     * @return An error ApiResponse instance with the provided details
     */
    public static ApiResponse<Void> error(String message, ErrorCode code) {
        return new ApiResponse<>(false, null, new ApiError(message, code));
    }

    /**
     * Internal helper method to build a Response with this ApiResponse as the entity.
     *
     * @param status The HTTP status code to use in the response
     * @return A Response object with the specified status and this instance as the entity
     */
    private Response buildResponseWithEntity(Response.Status status) {
        return Response.status(status).entity(this).build();
    }

    /**
     * Builds a Response with HTTP 201 (Created) status.
     *
     * @return A Response object with Created status and this instance as the entity
     */
    public Response buildCreatedResponse() {
        return buildResponseWithEntity(Response.Status.CREATED);
    }

    /**
     * Builds a Response with HTTP 400 (Bad Request) status.
     *
     * @return A Response object with Bad Request status and this instance as the entity
     */
    public Response buildBadRequestResponse() {
        return buildResponseWithEntity(Response.Status.BAD_REQUEST);
    }

    /**
     * Builds a Response with HTTP 200 (OK) status.
     *
     * @return A Response object with OK status and this instance as the entity
     */
    public Response buildOkResponse() {
        return buildResponseWithEntity(Response.Status.OK);
    }

    /**
     * Builds a Response with HTTP 204 (No Content) status.
     *
     * @return A Response object with No Content status
     */
    public Response buildNoContentResponse() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * Builds an error Response with a status code determined by the error code.
     * Maps different error codes to appropriate HTTP status codes.
     *
     * @return A Response object with a dynamically determined status code
     * @throws IllegalStateException if called on a success response
     */
    public Response buildDynamicErrorResponse() {
        if (error == null) {
            throw new IllegalStateException("Cannot build a dynamic error response for a success response.");
        }
        final Response.Status status = switch (error.code()) {
            case DUPLICATE_RESOURCE -> Response.Status.CONFLICT;
            case INTERNAL_SERVER_ERROR -> Response.Status.INTERNAL_SERVER_ERROR;
            default -> Response.Status.BAD_REQUEST;
        };
        return buildResponseWithEntity(status);
    }
}

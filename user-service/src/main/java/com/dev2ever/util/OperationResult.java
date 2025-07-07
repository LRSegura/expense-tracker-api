package com.dev2ever.util;

import com.dev2ever.model.ErrorCode;
import lombok.Getter;


/**
 * A generic wrapper class that represents the result of an operation, which can either be successful with a value
 * or contain error information. This class is commonly used for service layer operations to handle both success
 * and failure cases in a type-safe manner.
 *
 * @param <T> The type of value that this operation result will contain when successful
 */
@Getter
public class OperationResult<T> {
    private final T value;
    private final ErrorCode errorCode;
    private final String errorMessage;

    /**
     * Private constructor to enforce the use of static factory methods.
     *
     * @param value        The value of the operation results, null if the operation failed
     * @param errorCode    The error code if operation failed, null if successful
     * @param errorMessage The error message if operation failed, null if successful
     */
    private OperationResult(T value, ErrorCode errorCode, String errorMessage) {
        this.value = value;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a successful operation result with the provided value.
     *
     * @param <T>   The type of the value
     * @param value The value to be wrapped in the successful result
     * @return A new OperationResult instance representing a successful operation
     */
    public static <T> OperationResult<T> success(T value) {
        return new OperationResult<>(value, null, null);
    }

    /**
     * Creates a successful operation result without a value.
     * Useful for operations that don't return any value.
     *
     * @return A new OperationResult instance representing a successful operation with no value
     */
    public static OperationResult<Void> success() {
        return new OperationResult<>(null, null, null);
    }

    /**
     * Creates an error operation result with the specified error code and message.
     *
     * @param <T>     The type parameter for the operation result
     * @param error   The error code indicating the type of error
     * @param message The detailed error message
     * @return A new OperationResult instance representing a failed operation
     */
    public static <T> OperationResult<T> error(ErrorCode error, String message) {
        return new OperationResult<>(null, error, message);
    }

    /**
     * Checks if the operation was successful.
     *
     * @return true if the operation was successful (no error code present), false otherwise
     */
    public boolean isSuccess() {
        return errorCode == null;
    }

}


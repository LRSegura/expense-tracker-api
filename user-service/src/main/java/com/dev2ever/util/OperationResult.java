package com.dev2ever.util;

import com.dev2ever.model.ErrorCode;
import lombok.Getter;

@Getter
public class OperationResult<T> {
    private final T value;
    private final ErrorCode errorCode;
    private final String errorMessage;

    private OperationResult(T value, ErrorCode errorCode, String errorMessage) {
        this.value = value;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static <T> OperationResult<T> success(T value) {
        return new OperationResult<>(value, null, null);
    }

    public static OperationResult<Void> success() {
        return new OperationResult<>(null, null, null);
    }

    public static <T> OperationResult<T> error(ErrorCode error, String message) {
        return new OperationResult<>(null, error, message);
    }

    public boolean isSuccess() {
        return errorCode == null;
    }

}


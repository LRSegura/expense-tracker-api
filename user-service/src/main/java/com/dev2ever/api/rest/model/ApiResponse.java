package com.dev2ever.api.rest.model;

import com.dev2ever.model.ErrorCode;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ApiError error;

    private ApiResponse(boolean success, T data, ApiError error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> error(String message, ErrorCode code) {
        return new ApiResponse<>(false, null, new ApiError(message, code));
    }
}

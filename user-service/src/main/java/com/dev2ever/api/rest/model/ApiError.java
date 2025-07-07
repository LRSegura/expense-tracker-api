package com.dev2ever.api.rest.model;

import com.dev2ever.model.ErrorCode;

public record ApiError(String message, ErrorCode code) {
}

package com.lordkada.telr.services.errors;

import com.lordkada.telr.services.errors.beans.ServiceError;

public class ServiceErrorBuilder extends RuntimeException {

    public static ServiceError pokemonNotFound(String pokemonName) {
        return ServiceError.builder()
            .code(404)
            .message("Pokemon '" + pokemonName + "' not found")
            .build();
    }

    public static ServiceError quotaLimitsExceeded() {
        return ServiceError.builder()
            .code(429)
            .message("Exceeded usage limits")
            .build();
    }

    public static ServiceError genericError(String message) {
        return ServiceError.builder()
            .code(500)
            .message(message)
            .build();
    }

}
package com.lordkada.telr.usvcs.errors;

import com.lordkada.telr.usvcs.errors.beans.UsvcError;

public class UsvcErrorBuilder {

    public static UsvcError pokemonNotFound(String pokemonName) {
        return UsvcError.builder()
            .code(404)
            .message("Pokemon '" + pokemonName + "' not found")
            .build();
    }

    public static UsvcError malformedRemoteResponse(String response) {
        return UsvcError.builder()
            .code(400)
            .message(response)
            .build();
    }

    public static UsvcError genericError(String cause) {
        return UsvcError.builder()
            .code(500)
            .message("Caused: " + cause)
            .build();
    }

    public static UsvcError genericError() {
        return UsvcError.builder()
            .code(500)
            .message("No details")
            .build();
    }

    public static UsvcError quotaLimitsExceeded(String cause) {
        return UsvcError.builder()
            .code(429)
            .message(cause)
            .build();
    }

}
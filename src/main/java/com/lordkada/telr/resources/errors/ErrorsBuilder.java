package com.lordkada.telr.resources.errors;

import com.lordkada.telr.resources.errors.beans.APIError;

public class ErrorsBuilder {

    public static APIError notFound() {
        return APIError.builder()
            .code(404)
            .message("Item not found")
            .build();
    }

    public static APIError generic() {
        return APIError.builder()
            .code(500)
            .message("Internal error")
            .build();
    }

}


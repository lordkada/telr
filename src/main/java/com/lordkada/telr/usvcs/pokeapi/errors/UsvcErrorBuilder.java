package com.lordkada.telr.usvcs.pokeapi.errors;

import com.lordkada.telr.usvcs.pokeapi.errors.beans.UsvcError;

public class UsvcErrorBuilder {

    public static UsvcError pokemonNotFound(String pokemonName) {
        return UsvcError.builder()
            .code(404)
            .message("Pokemon '" + pokemonName + "' not found")
            .build();
    }

    public static UsvcError genericError(String cause) {
        return UsvcError.builder()
            .code(500)
            .message("Caused: " + cause)
            .build();
    }

}

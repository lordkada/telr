package com.lordkada.telr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Pokemon not found")
public class PokemonNotFoundException extends RuntimeException {

    private String pokemonName;

    public PokemonNotFoundException(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public String getPokemonName() {
        return this.pokemonName;
    }

}
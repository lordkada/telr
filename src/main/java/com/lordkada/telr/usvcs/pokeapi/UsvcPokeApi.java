package com.lordkada.telr.usvcs.pokeapi;

import java.util.concurrent.CompletableFuture;

public interface UsvcPokeApi {

    CompletableFuture<String> describe(String pokemonName);

}
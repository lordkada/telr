package com.lordkada.telr.usvcs.pokeapi;

import java.util.concurrent.CompletableFuture;

public interface UsvcPokeApi {

    String NO_DESCRIPTION_FOUND = "No flavor_text_entries found";

    CompletableFuture<String> describe(String pokemonName);

}
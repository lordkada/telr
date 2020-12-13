package com.lordkada.telr.services.translation;

import com.lordkada.telr.services.translation.beans.ShakespeareanResponseDTO;

import java.util.concurrent.CompletableFuture;

public interface TranslationService {

    CompletableFuture<ShakespeareanResponseDTO> translate(String pokemonName);

}

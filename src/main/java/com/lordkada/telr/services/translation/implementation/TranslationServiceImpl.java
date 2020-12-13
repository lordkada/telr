package com.lordkada.telr.services.translation.implementation;

import com.lordkada.telr.services.errors.ServiceErrorBuilder;
import com.lordkada.telr.services.translation.TranslationService;
import com.lordkada.telr.services.translation.beans.ShakespeareanResponseDTO;
import com.lordkada.telr.usvcs.errors.beans.UsvcError;
import com.lordkada.telr.usvcs.pokeapi.UsvcPokeApi;
import com.lordkada.telr.usvcs.translator.UsvcTranslator;

import java.util.concurrent.CompletableFuture;

public class TranslationServiceImpl implements TranslationService {

    private final UsvcPokeApi usvcPokeApi;
    private final UsvcTranslator usvcTranslator;

    public TranslationServiceImpl(UsvcPokeApi usvcPokeApi, UsvcTranslator usvcTranslator) {
        this.usvcPokeApi = usvcPokeApi;
        this.usvcTranslator = usvcTranslator;
    }

    @Override
    public CompletableFuture<ShakespeareanResponseDTO> translate(String pokemonName) {
        return usvcPokeApi.describe(pokemonName)
            .thenCompose(usvcTranslator::translate)
            .exceptionally(e -> {
                Throwable thr = e.getCause();
                if (thr instanceof UsvcError) {
                    UsvcError exc = (UsvcError) thr;

                    switch (exc.getCode()) {
                        case 404:
                            throw ServiceErrorBuilder.pokemonNotFound(pokemonName);
                        case 429:
                            throw ServiceErrorBuilder.quotaLimitsExceeded();
                        default:
                            throw ServiceErrorBuilder.genericError(exc.getMessage());
                    }
                }
                throw ServiceErrorBuilder.genericError(e.getMessage());
            })
            .thenApply(translated -> ShakespeareanResponseDTO.builder()
                .description(translated)
                .build());
    }

}
package com.lordkada.telr.configurations;

import com.lordkada.telr.services.translation.TranslationService;
import com.lordkada.telr.services.translation.beans.ShakespeareanResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

@Configuration
public class ServicesConfig {

    @Bean
    public TranslationService getTranslationService() {
        return new TranslationService() {
            @Override
            public CompletableFuture<ShakespeareanResponseDTO> translate(String pokemonName) {
                return CompletableFuture.completedFuture(ShakespeareanResponseDTO.builder().build());
            }
        };
    }

}
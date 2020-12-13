package com.lordkada.telr.configurations;

import com.lordkada.telr.services.translation.TranslationService;
import com.lordkada.telr.services.translation.implementation.TranslationServiceImpl;
import com.lordkada.telr.usvcs.pokeapi.UsvcPokeApi;
import com.lordkada.telr.usvcs.translator.UsvcTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    public TranslationService getTranslationService(UsvcPokeApi usvcPokeApi, UsvcTranslator usvcTranslator) {
        return new TranslationServiceImpl(usvcPokeApi, usvcTranslator);
    }

}
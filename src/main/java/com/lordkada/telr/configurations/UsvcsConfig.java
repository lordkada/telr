package com.lordkada.telr.configurations;

import com.lordkada.telr.usvcs.pokeapi.UsvcPokeApi;
import com.lordkada.telr.usvcs.pokeapi.implementation.UsvcPokeApiImpl;
import com.lordkada.telr.usvcs.translator.UsvcTranslator;
import com.lordkada.telr.usvcs.translator.implementation.UsvcTranslatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UsvcsConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UsvcPokeApi getUsvcPokeApi(RestTemplate restTemplate) {
        return new UsvcPokeApiImpl(restTemplate);
    }

    @Bean
    public UsvcTranslator getUsvcTranslator(RestTemplate restTemplate) {
        return new UsvcTranslatorImpl(restTemplate);
    }

}
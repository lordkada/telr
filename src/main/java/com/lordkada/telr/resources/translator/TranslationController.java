package com.lordkada.telr.resources.translator;

import com.lordkada.telr.resources.DTORepresentationHelpers;
import com.lordkada.telr.resources.ResourcesConstants;
import com.lordkada.telr.resources.translator.beans.ShakespeareanResponse;
import com.lordkada.telr.services.translation.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(ResourcesConstants.TRANSLATION)
public class TranslationController {

    @Autowired
    TranslationService translationService;

    @RequestMapping(method = RequestMethod.GET, value = "/{pokemonName}", produces = "application/json")
    public CompletableFuture<ShakespeareanResponse> translate(@PathVariable("pokemonName") String pokemonName) {
        return translationService.translate(pokemonName)
            .thenApply(shakespeareanResponseDTO ->
                DTORepresentationHelpers.instance(shakespeareanResponseDTO, pokemonName)
            );
    }

}
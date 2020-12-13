package com.lordkada.telr.services;

import com.lordkada.telr.services.errors.ServiceErrorBuilder;
import com.lordkada.telr.services.errors.beans.ServiceError;
import com.lordkada.telr.services.translation.TranslationService;
import com.lordkada.telr.services.translation.beans.ShakespeareanResponseDTO;
import com.lordkada.telr.services.translation.implementation.TranslationServiceImpl;
import com.lordkada.telr.usvcs.errors.UsvcErrorBuilder;
import com.lordkada.telr.usvcs.pokeapi.UsvcPokeApi;
import com.lordkada.telr.usvcs.translator.UsvcTranslator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
public class TranslatorServiceTest {

    @Mock
    private UsvcPokeApi usvcPokeApi;

    @Mock
    private UsvcTranslator usvcTranslator;

    @Test
    public void shouldTranslatePokemonAlaShakespearean() {
        String pokemonName = "myPokemon";
        String description = "This is the greatest piece of software in the world";
        String shakespeareanTranslation = "This is the greatest piece of software in the ordinary";

        doReturn(CompletableFuture.completedFuture(description))
            .when(usvcPokeApi)
            .describe(pokemonName);

        doReturn(CompletableFuture.completedFuture(shakespeareanTranslation))
            .when(usvcTranslator)
            .translate(description);

        TranslationService translationService = new TranslationServiceImpl(usvcPokeApi, usvcTranslator);

        ShakespeareanResponseDTO responseDTO = translationService.translate(pokemonName).join();

        assertEquals(shakespeareanTranslation, responseDTO.getDescription());

        InOrder inOrder = inOrder(usvcPokeApi, usvcTranslator);

        inOrder.verify(usvcPokeApi).describe(pokemonName);
        inOrder.verify(usvcTranslator).translate(description);

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldComplainForNotExistingPokemon() {
        String pokemonName = "notExistingPokemon";

        doReturn(CompletableFuture.supplyAsync(() -> {
                throw UsvcErrorBuilder.pokemonNotFound(pokemonName);
            })
        ).when(usvcPokeApi)
            .describe(pokemonName);

        TranslationService translationService = new TranslationServiceImpl(usvcPokeApi, usvcTranslator);

        try {
            translationService.translate(pokemonName).join();
            fail("Shouln't pass here");
        } catch (CompletionException e) {
            ServiceError serviceError = (ServiceError) e.getCause();
            Assertions.assertEquals(ServiceErrorBuilder.pokemonNotFound(pokemonName), serviceError);
        }
    }

    @Test
    public void shouldComplainForQuotalimit() {
        String pokemonName = "myPokemon";
        String description = "This is the greatest piece of software in the world";
        String errorMessage = "Quota limit";

        doReturn(CompletableFuture.completedFuture(description))
            .when(usvcPokeApi)
            .describe(pokemonName);

        doReturn(CompletableFuture.supplyAsync(() -> {
                throw UsvcErrorBuilder.quotaLimitsExceeded(errorMessage);
            })
        ).when(usvcTranslator)
            .translate(description);

        TranslationService translationService = new TranslationServiceImpl(usvcPokeApi, usvcTranslator);

        try {
            translationService.translate(pokemonName).join();
            fail("Shouln't pass here");
        } catch (CompletionException e) {
            ServiceError serviceError = (ServiceError) e.getCause();
            Assertions.assertEquals(ServiceErrorBuilder.quotaLimitsExceeded(), serviceError);
        }

    }

}

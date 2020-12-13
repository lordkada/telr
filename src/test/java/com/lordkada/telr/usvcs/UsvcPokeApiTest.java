package com.lordkada.telr.usvcs;

import com.lordkada.telr.usvcs.pokeapi.UsvcPokeApi;
import com.lordkada.telr.usvcs.pokeapi.errors.UsvcErrorBuilder;
import com.lordkada.telr.usvcs.pokeapi.errors.beans.UsvcError;
import com.lordkada.telr.usvcs.pokeapi.implementation.UsvcPokeApiImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletionException;

import static com.lordkada.telr.usvcs.pokeapi.UsvcPokeApi.NO_DESCRIPTION_FOUND;
import static com.lordkada.telr.usvcs.pokeapi.implementation.PokeApiConstants.POKEAPI_BASE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UsvcPokeApiTest {

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldFetchPokemanDescription() {
        String pokemonName = "existingPokemonName";
        String pokemonDescription = "This is the most incredible pokemon!";

        String jsonResponse = "{ \"flavor_text_entries\": " +
            "[" +
            "   {" +
            "       \"flavor_text\": \"Puede sobrevivir largo tiempo sin probar\\nbocado gracias a los nutrientes que guarda\\nen el bulbo del lomo.\"," +
            "       \"language\": {" +
            "           \"name\": \"es\"," +
            "           \"url\": \"https://pokeapi.co/api/v2/language/7/\"" +
            "       }," +
            "       \"version\": {" +
            "           \"name\": \"lets-go-pikachu\"," +
            "           \"url\": \"https://pokeapi.co/api/v2/version/31/\"" +
            "       }" +
            "   }," +
            "   {" +
            "       \"flavor_text\": \"" + pokemonDescription + "\"," +
            "       \"language\": {" +
            "           \"name\": \"en\"," +
            "           \"url\": \"https://pokeapi.co/api/v2/language/88/\"" +
            "       }," +
            "       \"version\": {" +
            "           \"name\": \"alpha-sapphire\"," +
            "           \"url\": \"https://pokeapi.co/api/v2/version/77/\"" +
            "       }" +
            "   }," +
            "   {" +
            "       \"flavor_text\": \"This is not the right description\"," +
            "       \"language\": {" +
            "           \"name\": \"en\"," +
            "           \"url\": \"https://pokeapi.co/api/v2/language/88/\"" +
            "       }," +
            "       \"version\": {" +
            "           \"name\": \"another-version\"," +
            "           \"url\": \"https://pokeapi.co/api/v2/version/77/\"" +
            "       }" +
            "   }" +
            "]" +
            "}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        Mockito.doReturn(responseEntity)
            .when(restTemplate)
            .exchange(eq(POKEAPI_BASE_URL + "/pokemon-species/" + pokemonName), eq(HttpMethod.GET), any(), eq(String.class));

        UsvcPokeApi usvcPokeApi = new UsvcPokeApiImpl(restTemplate);

        String description = usvcPokeApi.describe(pokemonName).join();
        Assertions.assertEquals(pokemonDescription, description);

        InOrder inOrder = Mockito.inOrder(restTemplate);
        inOrder.verify(restTemplate).exchange(eq(POKEAPI_BASE_URL + "/pokemon-species/" + pokemonName), eq(HttpMethod.GET), any(), eq(String.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldComplainOfNotExistingDescription() {
        String pokemonName = "pokemonWithoutDescription";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{}", HttpStatus.OK);

        Mockito.doReturn(responseEntity)
            .when(restTemplate)
            .exchange(eq(POKEAPI_BASE_URL + "/pokemon-species/" + pokemonName), eq(HttpMethod.GET), any(), eq(String.class));

        UsvcPokeApi usvcPokeApi = new UsvcPokeApiImpl(restTemplate);

        try {
            usvcPokeApi.describe(pokemonName).join();
            Assertions.fail("Shouldn't pass here");
        } catch (CompletionException e) {
            UsvcError usvcError = (UsvcError) e.getCause();
            Assertions.assertEquals(UsvcErrorBuilder.genericError(NO_DESCRIPTION_FOUND).getMessage(), usvcError.getMessage());
        }
    }

    @Test
    public void shouldComplainOfNotExistingPokemon() {
        String pokemonName = "NOTExistingPokemonName";

        Mockito.doThrow(UsvcErrorBuilder.pokemonNotFound(pokemonName))
            .when(restTemplate)
            .exchange(eq(POKEAPI_BASE_URL + "/pokemon-species/" + pokemonName), eq(HttpMethod.GET), any(), eq(String.class));

        UsvcPokeApi usvcPokeApi = new UsvcPokeApiImpl(restTemplate);

        try {
            usvcPokeApi.describe(pokemonName).join();
            Assertions.fail("Shouldn't pass here");
        } catch (CompletionException e) {
            UsvcError usvcError = (UsvcError) e.getCause();
            Assertions.assertEquals(UsvcErrorBuilder.pokemonNotFound(pokemonName), usvcError);
        }
    }

}
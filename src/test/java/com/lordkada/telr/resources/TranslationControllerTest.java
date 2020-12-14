package com.lordkada.telr.resources;


import com.lordkada.telr.resources.DTORepresentationHelpers;
import com.lordkada.telr.resources.ResourcesConstants;
import com.lordkada.telr.resources.errors.beans.APIError;
import com.lordkada.telr.resources.translator.beans.ShakespeareanResponse;
import com.lordkada.telr.services.errors.ServiceErrorBuilder;
import com.lordkada.telr.services.translation.TranslationService;
import com.lordkada.telr.services.translation.beans.ShakespeareanResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TranslationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    TranslationService translationService;

    @Test
    public void translationShouldReturnJson() {
        final String pokemonName = "charizard";
        String url = "http://localhost:" + port + ResourcesConstants.TRANSLATION + "/" + pokemonName;

        doReturn(CompletableFuture.completedFuture(ShakespeareanResponseDTO.builder().build())
        ).when(translationService)
            .translate(pokemonName);

        HttpHeaders httpHeaders = this.restTemplate.headForHeaders(url);
        assertThat(httpHeaders.getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    public void translationShouldReturnFilledResult() {
        final String pokemonName = "charizard";
        String url = "http://localhost:" + port + ResourcesConstants.TRANSLATION + "/" + pokemonName;

        doReturn(CompletableFuture.completedFuture(
            ShakespeareanResponseDTO.builder()
                .description(pokemonName + " ala Shakespeare")
                .build()
            )
        ).when(translationService)
            .translate(pokemonName);

        ShakespeareanResponse response = this.restTemplate.getForObject(url, ShakespeareanResponse.class);
        assertThat(response.getName().equals(pokemonName)).isTrue();
        assertThat(response.getDescription()).isEqualTo(pokemonName + " ala Shakespeare");

        InOrder inOrder = inOrder(translationService);
        inOrder.verify(translationService).translate(pokemonName);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void translationShouldReturn404ForMissingPokemonName() {
        final String pokemonName = "unknown";
        String url = "http://localhost:" + port + ResourcesConstants.TRANSLATION + "/" + pokemonName;

        Mockito.doThrow(ServiceErrorBuilder.pokemonNotFound(pokemonName))
            .when(translationService)
            .translate(pokemonName);

        ResponseEntity<APIError> exchange = this.restTemplate.exchange(url, HttpMethod.GET, null, APIError.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exchange.getBody()).isEqualTo(DTORepresentationHelpers.instance(ServiceErrorBuilder.pokemonNotFound(pokemonName)));
    }

}
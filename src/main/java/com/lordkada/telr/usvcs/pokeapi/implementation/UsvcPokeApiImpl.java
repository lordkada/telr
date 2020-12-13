package com.lordkada.telr.usvcs.pokeapi.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.lordkada.telr.usvcs.UsvcBase;
import com.lordkada.telr.usvcs.errors.UsvcErrorBuilder;
import com.lordkada.telr.usvcs.pokeapi.UsvcPokeApi;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

import static com.lordkada.telr.usvcs.pokeapi.implementation.PokeApiConstants.POKEAPI_BASE_URL;

public class UsvcPokeApiImpl extends UsvcBase implements UsvcPokeApi {

    public UsvcPokeApiImpl(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public CompletableFuture<String> describe(String pokemonName) {
        return CompletableFuture.supplyAsync(() -> {
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(POKEAPI_BASE_URL + "/pokemon-species/" + pokemonName, HttpMethod.GET, httpEntity, String.class);

                JsonNode root = mapper.readTree(response.getBody());
                JsonNode flavor_text_entries = root.get("flavor_text_entries");

                if (flavor_text_entries != null) {
                    JsonNode jsonNode = StreamSupport.stream(flavor_text_entries.spliterator(), false)
                        .filter(node -> node.get("language").get("name").asText().equals("en"))
                        .filter(node -> node.get("version").get("name").asText().equals(PokeApiConstants.GAME_VERSION))
                        .findFirst()
                        .orElseThrow(() -> UsvcErrorBuilder.pokemonNotFound(pokemonName));

                    return jsonNode.get("flavor_text").asText();
                }

                throw UsvcErrorBuilder.genericError(NO_DESCRIPTION_FOUND);
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw UsvcErrorBuilder.pokemonNotFound(pokemonName);
                }

                throw UsvcErrorBuilder.genericError(e.getMessage());
            } catch (JsonProcessingException e) {
                throw UsvcErrorBuilder.genericError(e.getMessage());
            }

        });
    }

}
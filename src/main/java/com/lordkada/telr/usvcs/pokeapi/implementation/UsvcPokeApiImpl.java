package com.lordkada.telr.usvcs.pokeapi.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lordkada.telr.usvcs.pokeapi.UsvcPokeApi;
import com.lordkada.telr.usvcs.pokeapi.errors.UsvcErrorBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

import static com.lordkada.telr.usvcs.pokeapi.implementation.PokeApiConstants.POKEAPI_BASE_URL;

public class UsvcPokeApiImpl implements UsvcPokeApi {

    final private RestTemplate restTemplate;

    public UsvcPokeApiImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CompletableFuture<String> describe(String pokemonName) {
        return CompletableFuture.supplyAsync(() -> {

            HttpHeaders headers = new HttpHeaders();
            headers.add("user-agent", "Telr/usvc-PokeApi");
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(POKEAPI_BASE_URL + "/pokemon-species/" + pokemonName, HttpMethod.GET, httpEntity, String.class);

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode flavor_text_entries = root.get("flavor_text_entries");
                JsonNode jsonNode = StreamSupport.stream(flavor_text_entries.spliterator(), false)
                    .filter(node -> node.get("language").get("name").asText().equals("en"))
                    .filter(node -> node.get("version").get("name").asText().equals(PokeApiConstants.GAME_VERSION))
                    .findFirst()
                    .orElseThrow(() -> UsvcErrorBuilder.pokemonNotFound(pokemonName));

                return jsonNode.get("flavor_text").asText();
            } catch (JsonProcessingException e) {
                throw UsvcErrorBuilder.genericError(e.getMessage());
            }

        });
    }

}
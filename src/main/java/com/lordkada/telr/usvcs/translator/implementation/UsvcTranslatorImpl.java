package com.lordkada.telr.usvcs.translator.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.lordkada.telr.usvcs.UsvcBase;
import com.lordkada.telr.usvcs.errors.UsvcErrorBuilder;
import com.lordkada.telr.usvcs.errors.beans.UsvcError;
import com.lordkada.telr.usvcs.pokeapi.implementation.PokeApiConstants;
import com.lordkada.telr.usvcs.translator.UsvcTranslator;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

import static com.lordkada.telr.usvcs.translator.implementation.TranslatorApiConstants.TRANSLATOR_BASE_URL;

public class UsvcTranslatorImpl extends UsvcBase implements UsvcTranslator {

    public UsvcTranslatorImpl(RestTemplate restTemplate) {
        super(restTemplate);

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Override
    public CompletableFuture<String> translate(String text) {
        return CompletableFuture.supplyAsync(() -> {

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("text", text);

            HttpEntity<?> formEntity = new HttpEntity<>(map, headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(TRANSLATOR_BASE_URL, HttpMethod.POST, formEntity, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    String body = response.getBody();
                    try {
                        JsonNode root = mapper.readTree(body);
                        return root.get("contents").get("translated").asText();
                    } catch (JsonProcessingException e) {
                        throw UsvcErrorBuilder.malformedRemoteResponse(body);
                    }
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    throw UsvcErrorBuilder.quotaLimitsExceeded(e.getMessage());
                }
            }

            throw UsvcErrorBuilder.genericError();
        });
    }

}
package com.lordkada.telr.usvcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class UsvcBase {

    final static protected ObjectMapper mapper = new ObjectMapper();

    final protected RestTemplate restTemplate;
    final protected HttpHeaders headers;

    public UsvcBase(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        headers = new HttpHeaders();
        headers.add("user-agent", "Telr/usvc-PokeApi");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

}
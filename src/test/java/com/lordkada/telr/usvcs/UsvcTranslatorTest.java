package com.lordkada.telr.usvcs;

import com.lordkada.telr.usvcs.errors.UsvcErrorBuilder;
import com.lordkada.telr.usvcs.errors.beans.UsvcError;
import com.lordkada.telr.usvcs.translator.UsvcTranslator;
import com.lordkada.telr.usvcs.translator.implementation.UsvcTranslatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletionException;

import static com.lordkada.telr.usvcs.translator.implementation.TranslatorApiConstants.TRANSLATOR_BASE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UsvcTranslatorTest {

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldTranslateText() {
        String toBeTranslated = "This is my text to be translated";
        String translatedText = "My fantastic translated text";

        String jsonResponse = "{" +
            "    \"success\": {" +
            "        \"total\": 1" +
            "    }," +
            "    \"contents\": {" +
            "        \"translated\": \"" + translatedText + "\",\n" +
            "        \"text\": \"" + toBeTranslated + "\",\n" +
            "        \"translation\": \"shakespeare\"\n" +
            "    }\n" +
            "}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        Mockito.doReturn(responseEntity)
            .when(restTemplate)
            .exchange(eq(TRANSLATOR_BASE_URL), eq(HttpMethod.POST), any(), eq(String.class));

        UsvcTranslator usvcTranslator = new UsvcTranslatorImpl(restTemplate);

        String result = usvcTranslator.translate(toBeTranslated).join();

        Assertions.assertEquals(translatedText, result);
    }

    @Test
    public void shouldComplainOfUsageLimit() {
        String toBeTranslated = "This is my text to be translated";

        String errorMessage = "Too Many Requests: Rate limit of 5 requests per hour exceeded. Please wait for 53 minutes and 37 seconds.";
        Mockito.doThrow(UsvcErrorBuilder.quotaLimitsExceeded(errorMessage))
            .when(restTemplate)
            .exchange(eq(TRANSLATOR_BASE_URL), eq(HttpMethod.POST), any(), eq(String.class));

        UsvcTranslator usvcTranslator = new UsvcTranslatorImpl(restTemplate);

        try {
            usvcTranslator.translate(toBeTranslated).join();
            Assertions.fail("Shouldn't pass here");
        } catch (CompletionException e) {
            UsvcError usvcError = (UsvcError) e.getCause();
            Assertions.assertEquals(UsvcErrorBuilder.quotaLimitsExceeded(errorMessage), usvcError);
        }

    }

}

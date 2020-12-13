package com.lordkada.telr.exceptions;

import com.lordkada.telr.resources.errors.beans.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ServiceErrorToHttpResponse {

    public static ResponseEntity<?> map(APIError APIErrorResponse) {
        final int code = APIErrorResponse.getCode();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        switch (code) {
            case 404:
                status = HttpStatus.NOT_FOUND;
                break;

            case 429:
                status = HttpStatus.TOO_MANY_REQUESTS;
                break;

            case 400:
                status = HttpStatus.BAD_REQUEST;
                break;

            case 500:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        final Map<String, Object> returnPayload = convertTotMap(APIErrorResponse);

        return new ResponseEntity<>(returnPayload, status);
    }

    public static Map<String, Object> convertTotMap(APIError APIErrorResponse) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", APIErrorResponse.getCode());
        error.put("message", APIErrorResponse.getMessage());
        return error;
    }

    public static String toJSON(APIError APIErrorResponse) {
        return "{" +
            "\"code\": \"" + APIErrorResponse.getCode() + "\", " +
            "\"message\": \"" + APIErrorResponse.getMessage() + "\"" +
            "}";
    }

}

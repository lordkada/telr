package com.lordkada.telr.exceptions;

import com.lordkada.telr.resources.DTORepresentationHelpers;
import com.lordkada.telr.resources.errors.beans.APIError;
import com.lordkada.telr.services.errors.beans.ServiceError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = ServiceError.class)
    public ResponseEntity<?> serviceErrorException(ServiceError ex) {
        APIError instance = DTORepresentationHelpers.instance(ex);
        return ServiceErrorToHttpResponse.map(instance);
    }

}
package com.lordkada.telr.services.errors.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceError extends RuntimeException {

    private int code;

    private String message;

}

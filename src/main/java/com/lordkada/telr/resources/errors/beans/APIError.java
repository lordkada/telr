package com.lordkada.telr.resources.errors.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APIError {

    private int code;

    private String message;

}
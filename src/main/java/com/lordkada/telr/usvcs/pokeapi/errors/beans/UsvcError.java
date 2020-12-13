package com.lordkada.telr.usvcs.pokeapi.errors.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsvcError extends RuntimeException {

    private int code;

    private String message;

}
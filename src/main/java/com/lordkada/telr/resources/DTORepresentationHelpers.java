package com.lordkada.telr.resources;

import com.lordkada.telr.resources.errors.beans.APIError;
import com.lordkada.telr.resources.translator.beans.ShakespeareanResponse;
import com.lordkada.telr.services.errors.beans.ServiceError;
import com.lordkada.telr.services.translation.beans.ShakespeareanResponseDTO;

public class DTORepresentationHelpers {

    public static ShakespeareanResponse instance(ShakespeareanResponseDTO shakespeareanResponseDTO, String pokemonName) {
        return ShakespeareanResponse.builder()
            .name(pokemonName)
            .description(shakespeareanResponseDTO.getDescription())
            .build();
    }

    public static APIError instance(ServiceError serviceError) {
        return APIError.builder()
            .code(serviceError.getCode())
            .message(serviceError.getMessage())
            .build();
    }
}
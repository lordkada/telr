package com.lordkada.telr.filters;

import com.lordkada.telr.exceptions.ServiceErrorToHttpResponse;
import com.lordkada.telr.resources.DTORepresentationHelpers;
import com.lordkada.telr.resources.errors.beans.APIError;
import com.lordkada.telr.services.errors.beans.ServiceError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ServiceError ex) {

            response.setHeader("Content-Type", "application/json");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,observe");

            final APIError APIErrorResponse = DTORepresentationHelpers.instance(ex);

            final ResponseEntity responseEntity = ServiceErrorToHttpResponse.map(APIErrorResponse);
            response.setStatus(responseEntity.getStatusCode().value());

            response.getWriter().write(ServiceErrorToHttpResponse.toJSON(APIErrorResponse));
            response.flushBuffer();
        }

    }

}
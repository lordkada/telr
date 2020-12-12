package com.lordkada.telr.resources.errors;

import com.lordkada.telr.resources.ResourcesConstants;
import com.lordkada.telr.resources.errors.beans.APIError;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class MyErrorController implements ErrorController {

    @RequestMapping(ResourcesConstants.ERROR)
    public APIError handleError(HttpServletRequest request) {

        if (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).equals(404)) {
            return ErrorsBuilder.notFound();
        }

        return ErrorsBuilder.generic();
    }

    @Override
    public String getErrorPath() {
        return ResourcesConstants.ERROR;
    }

}

package com.FirstProject.TinyURL.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UrlNotFoundException  extends Exception {

    public UrlNotFoundException(String message) {
        super(message);
    }

}

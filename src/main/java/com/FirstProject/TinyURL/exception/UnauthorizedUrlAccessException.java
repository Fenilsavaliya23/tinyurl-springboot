package com.FirstProject.TinyURL.exception;

public class UnauthorizedUrlAccessException extends RuntimeException{

    public UnauthorizedUrlAccessException(String message){
        super(message);
    }

}

package com.myLogin.login.error;

public class AuthorizeException extends Exception{

    public AuthorizeException() {
    }

    public AuthorizeException(String message) {
        super(message);
    }
}

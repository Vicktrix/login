package com.myLogin.login.controller;

import com.myLogin.login.error.AuthorizeException;
import com.myLogin.login.error.ErrorAdviceService;
import com.myLogin.login.error.RegistrationException;
import com.myLogin.login.error.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionController {
    private ErrorAdviceService errorAdviceService;

    public MyExceptionController(ErrorAdviceService errorAdviceService) {
        this.errorAdviceService = errorAdviceService;
    }
    
    @ExceptionHandler(AuthorizeException.class)
    @ResponseBody
    public ResponseEntity<Object> authorizeException(AuthorizeException auth) {
        System.out.println("\nauth = "+auth.getMessage());
        return new ResponseEntity(auth.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RegistrationException.class)
    @ResponseBody
    public ResponseEntity<Object> registrationException(RegistrationException reg) {
        System.out.println("\nreg = "+reg.getMessage());
        return new ResponseEntity(reg.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String registrationException(Exception ex) {
        return errorAdviceService.getResponceErrorPageWithMessage(
            "\n Something wrong on Server. Interrupted by Exception.class. <br>"
            + "\n Error : "+ex.getMessage());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public String registrationException(ResourceNotFoundException ex) {
        return errorAdviceService.getResponceErrorPageWithMessage(
            "\n Something wrong on Server. Interrupted by ResourceNotFoundException.class. <br>"
            + "\n Error : "+ex.getMessage());
    }
}
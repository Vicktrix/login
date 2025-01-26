package com.myLogin.login.controller;

import com.myLogin.login.error.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyControllerMVC {
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
    @GetMapping("/error")
    public String error() {
        return "error.html";
    }
    @GetMapping("/test")
    public String test() throws ResourceNotFoundException {
        if(true)
            throw new ResourceNotFoundException("Test from MVC controller");
        return "resources/error.html";
    }
}

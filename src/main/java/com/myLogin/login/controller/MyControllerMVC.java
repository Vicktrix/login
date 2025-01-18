package com.myLogin.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyControllerMVC {
    @GetMapping("/")
    public String home() {
        return "login.html";
    }
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
}

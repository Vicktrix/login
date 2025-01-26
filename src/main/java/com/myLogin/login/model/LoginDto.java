package com.myLogin.login.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginDto {
    @NotNull
    @NotBlank(message = "Please provide a username")
    @Size(min=3, max=15, message = "UserName must be at least 3 characters long and shorter then 15")
    private String username;
    @NotNull
    @Size(min=4, message = "Password must be at least 4 characters long")
    private String pass;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
};

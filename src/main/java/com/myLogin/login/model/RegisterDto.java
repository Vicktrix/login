package com.myLogin.login.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*validation from https://medium.com/@himani.prasad016/validations-in-spring-boot-e9948aa6286b*/

public class RegisterDto {
    @NotNull
    @NotBlank(message = "Please provide a username")
    @Size(min=3, max=15, message = "UserName must be at least 3 characters long and shorter then 15")
    private String username;
    
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @NotNull
    @NotBlank(message = "Please provide a username")
    @Size(min=3, max=15, message = "UserName must be at least 3 characters long and shorter then 15")
    private String fullName;
    
    @NotNull
    @Size(min=4, message = "Password must be at least 3 characters long")    
    private String pass;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

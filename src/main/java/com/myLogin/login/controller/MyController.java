package com.myLogin.login.controller;

import com.myLogin.login.model.LoginDto;
import com.myLogin.login.model.RegisterDto;
import com.myLogin.login.service.AppUserService;
import com.myLogin.login.service.TokenService;
import com.myLogin.login.error.*;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MyController {
    
    private AppUserService appUserService;
    private TokenService tokenService;
    
    public MyController(AppUserService appUserService, TokenService tokenService) {
        this.appUserService = appUserService;
        this.tokenService = tokenService;
    }
    
    @GetMapping("/guest/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.ok("\nThis is test!");
    }
    @GetMapping("/guest/test2")
    public ResponseEntity<Object> testException() throws ResourceNotFoundException {
        if(true) {
            throw new ResourceNotFoundException("\n There are test 2");
        }
        return null;
    }
    @GetMapping("/guest/test3")
    public ResponseEntity<Object> testException3() throws Exception {
        if(true) {
            throw new Exception("\n There are test 3");
        }
        return null;
    }
    @GetMapping("/all")
    public ResponseEntity<Object> showAll() {
        return ResponseEntity.ok(appUserService.fetchAllAppUsers());
    }
    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication auth) {
        System.out.println("\nInside Profile - correct token\n");
        Map<String, Object> response = new HashMap<>();
        response.put("userName", auth.getName());
        response.put("Authorities", auth.getAuthorities());
        response.put("AppUser", appUserService.getAppUserByName(auth.getName()).get());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/guest/login")
    public ResponseEntity<Object> userLogining(@RequestBody @Valid LoginDto login, BindingResult bindRes)
            throws AuthorizeException {
        if(bindRes.hasErrors()) {
            throw new AuthorizeException(errorParser(bindRes));
        }
        System.out.println("login.name = "+login.getUsername());
        System.out.println("login.pass = "+login.getPass());
        final ResponseEntity<Object> mapUserDetailsToResponseTest = 
            mapUserDetailsToResponse(appUserService.loginUserFromDB(login), login.getUsername());
        return mapUserDetailsToResponseTest;
    }
    
    @PostMapping("/guest/register")
    public ResponseEntity<Object> userRegistration(@RequestBody @Valid RegisterDto registerDto, BindingResult bindRes)
            throws RegistrationException, AuthorizeException {
        if(bindRes.hasErrors()) {
            throw new RegistrationException(errorParser(bindRes));
        }
        final ResponseEntity<Object> mapUserDetailsToResponseTest = 
            mapUserDetailsToResponse(appUserService.saveUserToDB(registerDto), registerDto.getUsername());
        return mapUserDetailsToResponseTest;
    }
    
    private ResponseEntity<Object> mapUserDetailsToResponse(Optional<UserDetails> user, String name)
            throws AuthorizeException {
        if(user.isEmpty()) {
            throw new AuthorizeException("User "+name+" not found in DB");
        }
        String token = tokenService.generateTokenFromUserDetail(user.get());
        System.out.println("user = "+user.get().getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("user", user.get());
        response.put("scope", user.get().getAuthorities().toString());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
    
    private String errorParser(BindingResult error) {
        return error.getFieldErrors().stream()
            .map(e -> e.getDefaultMessage()).collect(Collectors.joining(", "));
    }
    
    @PostConstruct
    public void controllerTest() {
        System.out.println("\nSuccessful bean-initialize, and testMthod - "+test().getBody().toString());
    }
}
package com.myLogin.login.controller;

import com.myLogin.login.model.LoginDto;
import com.myLogin.login.model.RegisterDto;
import com.myLogin.login.service.AppUserService;
import com.myLogin.login.service.TokenService;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
        return ResponseEntity.ok("This is test!");
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
    
    @PostMapping("/guest/register")
    public ResponseEntity<Object> userRegister(@RequestBody RegisterDto registerDto) {
        return mapUserDetailsToResponse(appUserService.saveUserToDB(registerDto));
    }
    @PostMapping("/guest/login")
    public ResponseEntity<Object> userLogining(@RequestBody LoginDto login) {
        System.out.println("login.name = "+login.getUsername());
        System.out.println("login.pass = "+login.getPass());
        return mapUserDetailsToResponse(appUserService.loginUserFromDB(login));

    }
    private ResponseEntity<Object> mapUserDetailsToResponse(Optional<UserDetails> user) {
        if(user.isEmpty())
            return ResponseEntity.badRequest().body(" User not registred in DB ");
        String token = tokenService.generateTokenFromUserDetail(user.get());
        System.out.println("user = "+user.get().getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("user", user.get());
        response.put("scope", user.get().getAuthorities().toString());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }    
    @PostConstruct
    public void controllerTest() {
        System.out.println("Successful bean-initialize, and testMthod - "+test().getBody().toString());
    }
}
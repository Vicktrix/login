package com.myLogin.login.config;

import com.myLogin.login.token.DecodeEncodeService;
import com.myLogin.login.token.RSAKeyPairGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class TokenConfig {
    
    @Bean
    public DecodeEncodeService decodeEncodeService() {
        return new DecodeEncodeService();
    }
    
    @Bean
    public RSAKeyPairGenerator pairGenerator() {
        return new RSAKeyPairGenerator();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }    
}
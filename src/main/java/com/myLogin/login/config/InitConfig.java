package com.myLogin.login.config;

import com.myLogin.login.service.AppUserService;
import com.myLogin.login.token.DecodeEncodeService;
import com.myLogin.login.token.RSAKeyPairGenerator;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.interfaces.RSAPublicKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class InitConfig {
        
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
    
    @Bean
    @DependsOn("pairGenerator")
    public JwtDecoder jwtDecoder(RSAKeyPairGenerator pairGenerator) {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey)pairGenerator.getPublicKey()).build();
    }
    
    @Bean
    @DependsOn("pairGenerator")
    public JwtEncoder jwtEncoder(RSAKeyPairGenerator pairGenerator) {
        JWK jwk = new RSAKey.Builder((RSAPublicKey)pairGenerator.getPublicKey())
                .privateKey(pairGenerator.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
    
    @Bean
    @DependsOn({"passwordEncoder", "appUserService"})
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, AppUserService appUserService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
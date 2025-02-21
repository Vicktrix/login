package com.myLogin.login.config;

import static java.util.Arrays.asList;
import java.util.function.Function;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Service
public class MyCorsConfig {
    
    public Function<String,Customizer<CorsConfigurer<HttpSecurity>>> getCorsWithSource = source -> cors -> 
            cors.configurationSource(setCors(source));
    
    private CorsConfigurationSource setCors(String ... externalSource) {
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(asList("http://localhost:8383"));
        config.setAllowedOrigins(asList(externalSource));
        config.setAllowedMethods(asList("HEAD","GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type", 
                "Access-Control-Allow-Origin", "Access-Control-Expose-Headers", "Access-Control-Allow-Headers"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

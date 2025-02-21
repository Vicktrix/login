package com.myLogin.login.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private MyCorsConfig myCorsConfig;

    public SecurityConfig(MyCorsConfig myCorsConfig) {
        this.myCorsConfig = myCorsConfig;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(myCorsConfig.getCorsWithSource.apply("http://localhost:8383"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/test","/resources/**", "/error", "/api/v1/guest/**").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/resources/login.html").permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/").permitAll())
            .oauth2ResourceServer(auth -> auth.jwt(withDefaults()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }
}

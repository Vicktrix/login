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
import static java.util.Arrays.asList;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private DecodeEncodeService decodeEncodeService;
    private RSAKeyPairGenerator keyPairGenerator;
    private PasswordEncoder encoder;

    public SecurityConfig(DecodeEncodeService decodeEncodeService, RSAKeyPairGenerator keyPairGenerator, PasswordEncoder encoder) {
        this.decodeEncodeService = decodeEncodeService;
        this.keyPairGenerator = keyPairGenerator;
        this.encoder = encoder;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
//            .cors(c -> c.disable())                   // set CORS as WebMvcConfigurer
            .cors(myCors)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/resources/**", "/error", "/api/v1/guest/**").permitAll()
                .anyRequest().authenticated())
//                .anyRequest().permitAll())
            .formLogin(form -> form
                .loginPage("/resources/login.html").permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/").permitAll())
            .oauth2ResourceServer(auth -> auth.jwt(withDefaults()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }
    
    private Customizer<CsrfConfigurer<HttpSecurity>> myCsrf = csrf -> {
                Set<String> allowedMethods = Set.of("GET", "HEAD", "TRACE", "OPTIONS");
                CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
                csrf
                    .requireCsrfProtectionMatcher(request ->
                        !allowedMethods.contains(request.getMethod()))
                    .requireCsrfProtectionMatcher(request -> request.getCookies()==null)
                    .ignoringRequestMatchers("/api/**")
                    .csrfTokenRepository(csrfTokenRepository)
                    .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                    .sessionAuthenticationStrategy(
                      new CsrfAuthenticationStrategy(csrfTokenRepository));
        };
    private Customizer<CsrfConfigurer<HttpSecurity>> stateLessTest = csrf -> {
            csrf.requireCsrfProtectionMatcher(request -> {
                    boolean yes = request.getCookies()==null;
                    System.out.print("\n don`t I use cookies(SessionCreationPolicy.STATELESS)? ");
                    System.out.println((yes? "Yes" : "No"));
                    return yes;
            });
    };
    
    private Customizer<CorsConfigurer<HttpSecurity>> myCors = cors -> {
        cors.configurationSource(corsConfigurationSource());    
    };
            
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
//        configuration.setAllowedOrigins(Arrays.asList("/**"));
//        configuration.setAllowedMethods(Arrays.asList("GET"));
//        configuration.setAllowedHeaders(List.of("Authorization"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
    
    /*
        https://stackoverflow.com/questions/62822393/spring-security-addcorsmappings-has-no-effect    
    */
    
//    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(asList("*"));
        configuration.setAllowedOrigins(asList("http://localhost:8383"));
        configuration.setAllowedMethods(asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(
                asList("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin",
                        "Access-Control-Expose-Headers", "Access-Control-Allow-Headers"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey)keyPairGenerator.getPublicKey()).build();
    }
    
    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder((RSAPublicKey)keyPairGenerator.getPublicKey())
                .privateKey(keyPairGenerator.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AppUserService userService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }
}

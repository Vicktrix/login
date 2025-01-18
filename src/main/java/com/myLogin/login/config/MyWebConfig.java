package com.myLogin.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MyWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry
                .addResourceHandler("/resources/**")
//                .addResourceHandler("/**")
                    .addResourceLocations("/public", "classpath:/static/")
////                .setCachePeriod(31556926);        // cache page to one year
                .setCachePeriod(10);                // cach page to 10 second
    }    
}

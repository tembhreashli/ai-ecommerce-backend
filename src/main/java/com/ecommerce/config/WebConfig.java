package com.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration
 * Configures Spring MVC settings
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure static resource handling
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // Configure content negotiation
        configurer
                .favorParameter(false)
                .ignoreAcceptHeader(false)
                .defaultContentType(org.springframework.http.MediaType.APPLICATION_JSON);
    }
}

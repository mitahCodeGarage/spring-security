package com.mitahcodegarage.poc.spring_security.config;


import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Permit access to health and info endpoints without authentication
                        //.requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                        // Secure all other Actuator endpoints with authentication
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).authenticated()
                        // Allow other application requests
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults()); // Use Customizer.withDefaults() instead of httpBasic()

        return http.build();
    }
}
package com.mitahcodegarage.poc.spring_security.config;


import com.mitahcodegarage.poc.spring_security.config.authentication.CustomAuthenticationProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan("com.mitahcodegarage.poc.spring_security.config.authentication")
public class SecurityConfig {

    @Autowired
    CustomAuthenticationProviderImpl customAuthenticationProvider;

    /**
     * The below is a sample chain to configure a APi with basic auth credentials
     * @param http
     * @return
     * @throws Exception
     */
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

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }
}

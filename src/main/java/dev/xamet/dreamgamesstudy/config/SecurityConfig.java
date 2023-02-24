package dev.xamet.dreamgamesstudy.config;

import dev.xamet.dreamgamesstudy.filter.AuthenticationFilter;
import dev.xamet.dreamgamesstudy.filter.FilterChainExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
     * Injection of the exception handler filter for the custom authentication filter.
     */
    @Autowired
    private FilterChainExceptionHandler filterChainExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> {                               // Note that access allows below can be altered upon the needs of the project
                    auth.requestMatchers("/user").permitAll(); // Allow access to the user endpoint - since it is used for registration
                    auth.requestMatchers(HttpMethod.GET).permitAll(); // Allow access to all GET requests - since it is only used for reading data
                })
                .addFilterBefore(new AuthenticationFilter(), BasicAuthenticationFilter.class) // Add the custom authentication filter when api calls are made
                .addFilterBefore(filterChainExceptionHandler, AuthenticationFilter.class) // Add the exception handler filter for the custom authentication filter
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}

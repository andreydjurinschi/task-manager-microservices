package com.example.userservice.config;

import com.example.userservice.dao.UserDAOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDAOImpl userRepository;

    public SecurityConfig(UserDAOImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL выхода
                        .logoutSuccessHandler((request, response, authentication) -> {
                            String logoutUrl = "http://localhost:8083/realms/task-manager/protocol/openid-connect/logout?redirect_uri=http://localhost:8081/";
                            response.sendRedirect(logoutUrl);
                        }))
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/login"
                        ).permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/users").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/manager").hasRole("MANAGER")
                        .requestMatchers("/teacher").hasRole("TEACHER")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(
                                        new KeycloakJwtAuthenticationConverter(userRepository)
                                )
                        )
                );

        return http.build();
    }
}

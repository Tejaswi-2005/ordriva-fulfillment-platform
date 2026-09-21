package com.ordriva.auth.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required"))
                        .accessDeniedHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient permissions"))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/healthz", "/auth/register", "/auth/login", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/products/**", "/v1/warehouses/**", "/v1/inventory/**", "/v1/orders/**").hasAnyRole("ADMIN", "OPERATIONS_MANAGER", "WAREHOUSE_MANAGER", "SUPPORT_AGENT", "VIEWER")
                        .requestMatchers(HttpMethod.POST, "/v1/orders").hasAnyRole("ADMIN", "OPERATIONS_MANAGER", "SUPPORT_AGENT")
                        .requestMatchers(HttpMethod.POST, "/v1/orders/*/cancel").hasAnyRole("ADMIN", "OPERATIONS_MANAGER", "SUPPORT_AGENT")
                        .requestMatchers("/v1/products/**").hasAnyRole("ADMIN", "OPERATIONS_MANAGER")
                        .requestMatchers("/v1/warehouses/**").hasAnyRole("ADMIN", "OPERATIONS_MANAGER", "WAREHOUSE_MANAGER")
                        .requestMatchers("/v1/inventory/**").hasAnyRole("ADMIN", "OPERATIONS_MANAGER", "WAREHOUSE_MANAGER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
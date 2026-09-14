package com.autovibe.dealership;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtService jwtService, CustomUserDetailsService userDetailsService)
      throws Exception {

    JwtAuthenticationFilter jwtAuthenticationFilter =
        new JwtAuthenticationFilter(jwtService, userDetailsService);

    http.csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.dispatcherTypeMatchers(DispatcherType.ERROR)
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/cars/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/users/register")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/login")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/cars/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/cars/**")
                    .hasRole("ADMIN")
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                    ).permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/cars/**")
                    .hasRole("ADMIN")
                    
                    .requestMatchers(HttpMethod.GET, "/customers/**")
                    .hasAnyRole("ADMIN", "SALESPERSON")

                    .requestMatchers(HttpMethod.POST, "/customers/**")
                    .hasAnyRole("ADMIN", "SALESPERSON")

                    .requestMatchers(HttpMethod.PUT, "/customers/**")
                    .hasAnyRole("ADMIN", "SALESPERSON")

                    .requestMatchers(HttpMethod.DELETE, "/customers/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/sales/**")
                    .hasAnyRole("ADMIN", "SALESPERSON")

                    .requestMatchers(HttpMethod.POST, "/sales/**")
                    .hasAnyRole("ADMIN", "SALESPERSON")
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                        (request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .accessDeniedHandler(
                        (request, response, accessDeniedException) ->
                            response.sendError(HttpServletResponse.SC_FORBIDDEN)))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

    provider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(provider);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("http://localhost:5173"));

    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}

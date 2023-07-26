package nl.leonklute.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

import static nl.leonklute.backend.service.KeycloakUserService.DEFAULT_PASSWORD;
import static nl.leonklute.backend.service.KeycloakUserService.DEFAULT_USER;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));
        http.csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .requestMatchers("/login", "/register").permitAll()
                                .requestMatchers("/api/**").authenticated()

                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                                (request, response, authException) -> response.setStatus(401))
                )
                .httpBasic(withDefaults())
                .formLogin(formLogin -> {
                    formLogin.successHandler((request, response, authentication) -> {
                        response.setStatus(200);
                    });
                    formLogin.failureHandler((request, response, exception) -> {
                        response.setStatus(401);
                    });
                });
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("content-type"));
        configuration.setAllowCredentials(true);
        source.registerCorsConfiguration("/api/**", configuration);

        CorsConfiguration loginConfiguration = new CorsConfiguration();
        loginConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        loginConfiguration.setAllowedMethods(List.of("POST", "OPTIONS"));
        loginConfiguration.setAllowedHeaders(List.of("*"));
        loginConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/login", loginConfiguration);
        return source;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User
                .builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(DEFAULT_USER)
                .password(DEFAULT_PASSWORD)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    PasswordEncoder BCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
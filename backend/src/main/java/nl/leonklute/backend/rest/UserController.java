package nl.leonklute.backend.rest;

import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.domain.RegisterRequest;
import nl.leonklute.backend.service.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static nl.leonklute.backend.service.KeycloakUserService.DEFAULT_PASSWORD;
import static nl.leonklute.backend.service.KeycloakUserService.DEFAULT_USER;

@RestController
public class UserController {

    private final KeycloakUserService keycloakUserService;
private InMemoryUserDetailsManager inMemoryUserDetailsManager;
private PasswordEncoder passwordEncoder;
    @Autowired
    public UserController(KeycloakUserService keycloakUserService, InMemoryUserDetailsManager inMemoryUserDetailsManager, PasswordEncoder passwordEncoder) {
        this.keycloakUserService = keycloakUserService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public KeycloakUser register(RegisterRequest user) {
        UserDetails userDetails = User
                .builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
        inMemoryUserDetailsManager.createUser(userDetails);
        KeycloakUser kcUser = new KeycloakUser();
        kcUser.setUsername(user.getUsername());
        return keycloakUserService.create(kcUser);
    }
}

package nl.leonklute.backend.service;

import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.repository.KeycloakUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class KeycloakUserService {
    public static final String DEFAULT_USER = "default";
    public static final String DEFAULT_PASSWORD = "password";

    private final KeycloakUserRepository keycloakUserRepository;

    @Autowired
    public KeycloakUserService(KeycloakUserRepository keycloakUserRepository,
                               InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.keycloakUserRepository = keycloakUserRepository;
        UserDetails defaultUser = inMemoryUserDetailsManager.loadUserByUsername(DEFAULT_USER);
        KeycloakUser user = this.getUser(defaultUser.getUsername()).orElseGet(KeycloakUser::new);
        user.setUsername(DEFAULT_USER);
        this.keycloakUserRepository.save(user);
    }

    public void createDefaultUser(){
        var user = this.getUser(DEFAULT_USER).orElseGet(KeycloakUser::new);
        user.setUsername(DEFAULT_USER);
        this.keycloakUserRepository.save(user);
    }
    public Optional<KeycloakUser> getUser(String username) {
        return keycloakUserRepository.findByUsername(username);
    }

    public KeycloakUser create(KeycloakUser user) {
        return keycloakUserRepository.save(user);
    }

    public void delete(KeycloakUser user) {
        keycloakUserRepository.delete(user);
    }

    public KeycloakUser getDefault() {
        return getUser(DEFAULT_USER).orElseThrow(() -> new RuntimeException("Default user does not exist"));
    }
}

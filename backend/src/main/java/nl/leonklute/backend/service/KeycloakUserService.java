package nl.leonklute.backend.service;

import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.repository.KeycloakUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KeycloakUserService {

    private final String DEFAULT_USER = "default";
    private final KeycloakUserRepository keycloakUserRepository;

    @Autowired
    public KeycloakUserService(KeycloakUserRepository keycloakUserRepository) {
        this.keycloakUserRepository = keycloakUserRepository;
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

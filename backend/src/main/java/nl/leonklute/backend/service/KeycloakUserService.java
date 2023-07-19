package nl.leonklute.backend.service;

import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.repository.KeycloakUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KeycloakUserService {
    private final KeycloakUserRepository keycloakUserRepository;

    @Autowired
    public KeycloakUserService(KeycloakUserRepository keycloakUserRepository) {
        this.keycloakUserRepository = keycloakUserRepository;
    }

    public void createDefaultUser(){
        var user = this.getUser("default").orElseGet(KeycloakUser::new);
        user.setUsername("default");
        this.keycloakUserRepository.save(user);
    }
    public Optional<KeycloakUser> getUser(String username) {
        return keycloakUserRepository.findByUsername(username);
    }
}

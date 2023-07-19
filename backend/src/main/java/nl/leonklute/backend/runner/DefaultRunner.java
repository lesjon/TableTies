package nl.leonklute.backend.runner;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.backend.TableSetter;
import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.service.PeopleService;
import nl.leonklute.backend.service.RelationService;
import nl.leonklute.backend.service.TableGroupService;
import nl.leonklute.backend.service.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultRunner implements ApplicationRunner {
    private final KeycloakUserService keycloakUserService;
    private final PeopleService peopleService;
    private final RelationService relationService;
    private final TableSetter tableSetter;
    private final TableGroupService tableGroupService;

    @Autowired
    public DefaultRunner(KeycloakUserService keycloakUserService, PeopleService peopleService,
                         RelationService relationService, TableSetter tableSetter, TableGroupService tableGroupService) {
        this.keycloakUserService = keycloakUserService;
        this.peopleService = peopleService;
        this.relationService = relationService;
        this.tableSetter = tableSetter;
        this.tableGroupService = tableGroupService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        keycloakUserService.createDefaultUser();
        log.debug("Default user created");
        KeycloakUser keycloakUser = keycloakUserService.getUser("default").get();
        log.debug("Default user retrieved");
        peopleService.loadDefaults(keycloakUser);
        log.debug("Default people loaded");
        relationService.loadDefaults(keycloakUser);
        log.debug("Default relations loaded");
        tableGroupService.loadDefaults(keycloakUser);
        log.debug("Default tables loaded");
        int[] tables = tableSetter.createTables(peopleService.getAllPeopleByUser(keycloakUser), relationService.getAllRelationsByUser(keycloakUser), tableGroupService.getAllTablesByUser(keycloakUser));
        log.debug("Tables created: {}", tables);
    }
}

package nl.leonklute.backend.runner;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.backend.TableSetter;
import nl.leonklute.backend.domain.Event;
import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.service.*;
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
    private final EventService eventService;

    @Autowired
    public DefaultRunner(KeycloakUserService keycloakUserService, PeopleService peopleService,
                         RelationService relationService, TableSetter tableSetter, TableGroupService tableGroupService, EventService eventService) {
        this.keycloakUserService = keycloakUserService;
        this.peopleService = peopleService;
        this.relationService = relationService;
        this.tableSetter = tableSetter;
        this.tableGroupService = tableGroupService;
        this.eventService = eventService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        keycloakUserService.createDefaultUser();
        log.debug("Default user created");
        KeycloakUser keycloakUser = keycloakUserService.getDefault();
        log.debug("Default user retrieved");
        Event event = eventService.getDefaultEvent(keycloakUser);
        log.debug("Default user retrieved");
        peopleService.loadDefaults(event);
        log.debug("Default people loaded");
        relationService.loadDefaults(event);
        log.debug("Default relations loaded");
        tableGroupService.loadDefaults(event);
        log.debug("Default tables loaded");
        int[] tables = tableSetter.createTables(peopleService.getAllPeopleByEvent(event), relationService.getAllRelationsByUser(event), tableGroupService.getAllTablesByEvent(event));
        log.debug("Tables created: {}", tables);
    }
}

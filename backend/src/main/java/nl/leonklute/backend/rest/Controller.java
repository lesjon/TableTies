package nl.leonklute.backend.rest;

import nl.leonklute.backend.TableSetter;
import nl.leonklute.backend.domain.*;
import nl.leonklute.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api")
public class Controller {
    private final KeycloakUserService keycloakUserService;
    private final TableGroupService tableGroupService;
    private final PeopleService peopleService;
    private final RelationService relationService;
    private final EventService eventService;
    private final TableSetter tableSetter;

    @Autowired
    public Controller(KeycloakUserService keycloakUserService, TableGroupService tableGroupService, PeopleService peopleService, RelationService relationService, EventService eventService, TableSetter tableSetter) {
        this.keycloakUserService = keycloakUserService;
        this.tableGroupService = tableGroupService;
        this.peopleService = peopleService;
        this.relationService = relationService;
        this.eventService = eventService;
        this.tableSetter = tableSetter;
    }
    @GetMapping(value = "/info", produces = "application/json")
    public String hello() {
        return "Table ties api";
    }

    @GetMapping(value = "/user", produces = "application/json")
    public KeycloakUser getUser(@AuthenticationPrincipal String userName) {
        var user = keycloakUserService.getUser(userName);
        return user.orElse(keycloakUserService.getDefault());
    }

    @GetMapping(value = "/table", produces = "application/json")
    public List<TableGroup> getTables() {
        Event defaultEvent = eventService.getDefaultEvent(keycloakUserService.getDefault());
        return tableGroupService.getAllTablesByEvent(defaultEvent);
    }

    @PostMapping(value = "/table", produces = "application/json")
    public TableGroup createTable(TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    @GetMapping(value = "/event", produces = "application/json")
    public List<Event> getEvents() {
        return eventService.getAllEventsByUser(keycloakUserService.getDefault());
    }

    @PostMapping(value = "/event", produces = "application/json")
    public Event createEvent(Event event) {
        return eventService.create(event);
    }

    @PostMapping(value = "/event/{eventId}/compute", produces = "application/json")
    public List<Integer> createRelation(Integer eventId) {
        Event event = eventService.getEvent(eventId).orElseThrow(() -> new RuntimeException("Event does not exist"));
        List<Person> people = peopleService.getAllPeopleByEvent(event);
        List<Relation> relations = relationService.getAllRelationsByUser(event);
        List<TableGroup> tables = tableGroupService.getAllTablesByEvent(event);
        int[] groups = tableSetter.createTables(people, relations, tables);
        return Arrays.stream(groups).boxed().toList();
    }
    @GetMapping(value = "/person", produces = "application/json")
    public List<Person> getPeople(@AuthenticationPrincipal String userName) {
        Event defaultEvent = eventService.getDefaultEvent(keycloakUserService.getDefault());
        return peopleService.getAllPeopleByEvent(defaultEvent);
    }
    @PostMapping(value = "/person", produces = "application/json")
    public Person createPerson(Person person) {
        return peopleService.create(person);
    }

    @GetMapping(value = "/relation", produces = "application/json")
    public List<Relation> getRelations(@AuthenticationPrincipal String userName) {
        Event defaultEvent = eventService.getDefaultEvent(keycloakUserService.getDefault());
        return relationService.getAllRelationsByUser(defaultEvent);
    }
    @PostMapping(value = "/relation", produces = "application/json")
    public Relation createRelation(Relation relation) {
        return relationService.create(relation);
    }
}

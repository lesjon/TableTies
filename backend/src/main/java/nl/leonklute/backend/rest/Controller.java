package nl.leonklute.backend.rest;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.backend.TableSetter;
import nl.leonklute.backend.domain.*;
import nl.leonklute.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Slf4j
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
    public KeycloakUser getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return getKeycloakUser(userDetails);
    }

    @GetMapping(value = "/event", produces = "application/json")
    public List<Event> getEvents(@AuthenticationPrincipal UserDetails userDetails) {
        var user = getKeycloakUser(userDetails);
        return eventService.getAllEventsByUser(user);
    }

    @PostMapping(value = "/event", produces = "application/json")
    public Event createEvent(@AuthenticationPrincipal UserDetails userDetails, Event event) {
        var user = getKeycloakUser(userDetails);
        event.setKeycloakUser(user);
        return eventService.create(event);
    }

    @GetMapping(value = "/event/{eventId}/table", produces = "application/json")
    public List<TableGroup> getTables(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId) {
        var user = getKeycloakUser(userDetails);
        Event event = getEvent(eventId);
        validateEvent(event, user);
        return tableGroupService.getAllTablesByEvent(event);
    }

    @PostMapping(value = "/event/{eventId}/table", produces = "application/json")
    public TableGroup createTable(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId, TableGroup tableGroup) {
        var user = getKeycloakUser(userDetails);
        Event event = getEvent(eventId);
        validateEvent(event, user);
        validateTableGroup(tableGroup, event);
        return tableGroupService.create(tableGroup);
    }

    @PostMapping(value = "/event/{eventId}/compute", produces = "application/json")
    public List<Integer> createRelation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId) {
        var user = getKeycloakUser(userDetails);
        Event event = eventService.getEvent(eventId).orElseThrow(() -> new RuntimeException("Event does not exist"));
        validateEvent(event, user);
        List<Person> people = peopleService.getAllPeopleByEvent(event);
        List<Relation> relations = relationService.getAllRelationsByUser(event);
        List<TableGroup> tables = tableGroupService.getAllTablesByEvent(event);
        int[] groups = tableSetter.createTables(people, relations, tables);
        return Arrays.stream(groups).boxed().toList();
    }
    @GetMapping(value = "/event/{eventId}/person", produces = "application/json")
    public List<Person> getPeople(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId) {
        var user = getKeycloakUser(userDetails);
        Event event = getEvent(eventId);
        validateEvent(event, user);
        return peopleService.getAllPeopleByEvent(event);
    }
    @PostMapping(value = "/event/{eventId}/person", produces = "application/json")
    public Person createPerson(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId, Person person) {
        var user = getKeycloakUser(userDetails);
        Event event = getEvent(eventId);
        validateEvent(event, user);
        return peopleService.create(person);
    }

    @GetMapping(value = "/event/{eventId}/relation", produces = "application/json")
    public List<Relation> getRelations(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId) {
        var user = getKeycloakUser(userDetails);
        Event event = getEvent(eventId);
        validateEvent(event, user);
        return relationService.getAllRelationsByUser(event);
    }
    @PostMapping(value = "/event/{eventId}/relation", produces = "application/json")
    public Relation createRelation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId, Relation relation) {
        var user = getKeycloakUser(userDetails);
        Event event = getEvent(eventId);
        validateEvent(event, user);
        validateRelation(relation, event);
        return relationService.create(relation);
    }

    private KeycloakUser getKeycloakUser(UserDetails userDetails) {
        var user = keycloakUserService.getUser(userDetails.getUsername());
        return user.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Event getEvent(@PathVariable Integer eventId) {
        return eventService.getEvent(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    private void validateEvent(Event event, KeycloakUser user) {
        if(event.getKeycloakUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }
    }
    private void validateRelation(Relation relation, Event event) {
        if(!relation.getEvent().equals(event)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Relation does not belong to event");
        }
    }

    private void validateTableGroup(TableGroup tableGroup, Event event) {
        if(!tableGroup.getEvent().equals(event)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Table does not belong to event");
        }
    }
}

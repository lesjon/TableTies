package nl.leonklute.backend.rest;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.backend.TableSetter;
import nl.leonklute.backend.domain.*;
import nl.leonklute.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    public Event createEvent(@AuthenticationPrincipal UserDetails userDetails, @RequestBody EventRequest eventRequest) {
        log.debug("Creating event for user {}", userDetails.getUsername());
        var user = getKeycloakUser(userDetails);
        log.debug("Creating event for user {}", user);
        var event = new Event();
        event.setKeycloakUser(user);
        event.setName(eventRequest.getName());
        log.debug("Creating event {}", event);
        return eventService.create(event);
    }
    @GetMapping(value = "/event/{eventId}", produces = "application/json")
    public Event getEvent(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId) {
        log.debug("Getting event {} for user {}", eventId, userDetails.getUsername());
        var user = getKeycloakUser(userDetails);
        Event event = getEventById(eventId);
        validateEvent(event, user);
        return event;
    }

    @GetMapping(value = "/event/{eventId}/table", produces = "application/json")
    public List<TableGroup> getTables(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId) {
        var user = getKeycloakUser(userDetails);
        Event event = getEventById(eventId);
        validateEvent(event, user);
        return tableGroupService.getAllTablesByEvent(event);
    }

    @PostMapping(value = "/event/{eventId}/table", produces = "application/json")
    public TableGroup createTable(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId, @RequestBody TableGroupRequest tableGroupRequest) {
        var user = getKeycloakUser(userDetails);
        Event event = getEventById(eventId);
        validateEvent(event, user);
        validateTableGroup(tableGroupRequest, event);
        var tableGroup = new TableGroup();
        tableGroup.setEvent(event);
        tableGroup.setCapacity(tableGroupRequest.getCapacity());
        tableGroup.setTarget(tableGroupRequest.getTarget());
        if(tableGroup.getCapacity() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Capacity is required");
        }
        return tableGroupService.create(tableGroup);
    }
    @DeleteMapping(value = "/event/{eventId}/table/{tableId}")
    public void deleteTable(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId, @PathVariable Integer tableId) {
        var user = getKeycloakUser(userDetails);
        Event event = getEventById(eventId);
        validateEvent(event, user);
        TableGroup tableGroup = getTableGroupById(tableId);
        tableGroupService.delete(tableGroup.getId());
    }

    private TableGroup getTableGroupById(Integer tableId) {
        return tableGroupService.findTableGroupById(tableId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));
    }

    @PostMapping(value = "/event/{eventId}/compute", produces = "application/json")
    public List<Integer> computeTableGroups(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId) {
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
        Event event = getEventById(eventId);
        validateEvent(event, user);
        return peopleService.getAllPeopleByEvent(event);
    }

    @PostMapping(value = "/event/{eventId}/person", produces = "application/json")
    public Person createPerson(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId, @RequestBody PersonRequest personRequest) {
        var user = getKeycloakUser(userDetails);
        Event event = getEventById(eventId);
        validateEvent(event, user);
        var person = new Person();
        person.setEvent(event);
        person.setName(personRequest.getName());
        return peopleService.create(person);
    }

    @DeleteMapping(value = "/event/{eventId}/person/{personId}")
    public void deletePerson(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId, @PathVariable Integer personId) {
        var user = getKeycloakUser(userDetails);
        Event event = getEventById(eventId);
        validateEvent(event, user);
        Person person = peopleService.getPersonById(personId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!person.getEvent().equals(event)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        peopleService.delete(person);
    }

    @GetMapping(value = "/event/{eventId}/relation", produces = "application/json")
    public List<Relation> getRelations(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId) {
        var user = getKeycloakUser(userDetails);
        Event event = getEventById(eventId);
        validateEvent(event, user);
        return relationService.getAllRelationsByUser(event);
    }

    @PostMapping(value = "/event/{eventId}/relation", produces = "application/json")
    public Relation createRelation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer eventId, @RequestBody RelationRequest relationRequest) {
        var user = getKeycloakUser(userDetails);
        Event event = getEventById(eventId);
        validateEvent(event, user);
        validateRelation(relationRequest, event);
        var relation = new Relation();
        relation.setEvent(event);
        var personId1 = relationRequest.getPerson1() < relationRequest.getPerson2() ? relationRequest.getPerson1() : relationRequest.getPerson2();
        var personId2 = relationRequest.getPerson1() < relationRequest.getPerson2() ? relationRequest.getPerson2() : relationRequest.getPerson1();
        relation.setPerson1(peopleService.getPersonById(personId1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("%s not found", personId1))));
        relation.setPerson2(peopleService.getPersonById(personId2)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("%s not found", personId2))));
        relation.setRelationStrength(relationRequest.getRelationStrength());
        return relationService.createOrUpdate(relation);
    }

    private KeycloakUser getKeycloakUser(UserDetails userDetails) {
        var user = keycloakUserService.getUser(userDetails.getUsername());
        return user.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Event getEventById(Integer eventId) {
        return eventService.getEvent(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    private void validateEvent(Event event, KeycloakUser user) {
        log.debug("Validating event {} for user {}", event, user);
        if(!event.getKeycloakUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }
    }
    private void validateRelation(RelationRequest relation, Event event) {
        if(!relation.getEventId().equals(event.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Relation does not belong to event");
        }
    }

    private void validateTableGroup(TableGroupRequest tableGroup, Event event) {
        if(!tableGroup.getEventId().equals(event.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Table does not belong to event");
        }
    }
}

package nl.leonklute.backend.service;

import nl.leonklute.backend.domain.Event;
import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    private final String DEFAULT_EVENT = "default_event";

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    public void createDefaultEvent(KeycloakUser user){
        var event = this.getEvent(DEFAULT_EVENT).orElseGet(Event::new);
        event.setKeycloakUser(user);
        event.setName(DEFAULT_EVENT);
        this.create(event);
    }

    public Event getDefaultEvent(KeycloakUser user) {
        this.createDefaultEvent(user);
        return getEvent(DEFAULT_EVENT).orElseThrow(() -> new RuntimeException("Default event does not exist"));
    }

    public Optional<Event> getEvent(String eventName) {
        return eventRepository.findByName(eventName);
    }

    public Optional<Event> getEvent(Integer eventId) {
        return eventRepository.findById(eventId);
    }

    public Event create(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEventsByUser(KeycloakUser user) {
        return eventRepository.findAllByKeycloakUser(user);
    }
}

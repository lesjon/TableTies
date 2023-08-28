package nl.leonklute.backend.service;

import nl.leonklute.backend.domain.Event;
import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.domain.Person;
import nl.leonklute.backend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    private final PersonRepository personRepository;

    @Value("classpath:people.csv")
    Resource defaultPeople;
    @Autowired
    public PeopleService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private List<Person> readPeopleFromFile(String peopleFileName) throws IOException {
        List<Person> people = new ArrayList<>();

        try (var peopleFile = new BufferedReader(new FileReader(peopleFileName))) {
            while (peopleFile.ready()) {
                String nameLine = peopleFile.readLine();
                var person = new Person();
                person.setName(nameLine);
                people.add(person);
            }
        }
        return people;
    }

    public void loadDefaults(Event event) throws IOException {
        List<Person> people = readPeopleFromFile(defaultPeople.getFile().getAbsolutePath());
        for (var person : people) {
            person = personRepository.getByName(person.getName()).orElse(person);
            person.setName(person.getName());
            person.setEvent(event);
            create(person);
        }
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }

    public List<Person> getAllPeopleByEvent(Event event) {
        return personRepository.findAllByEvent(event);
    }

    public Optional<Person> getPersonByName(String name) {
        return personRepository.getByName(name);
    }

    public Optional<Person> getPersonById(Integer personId) {
        return personRepository.findById(personId);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }
    public Optional<Person> deleteById(Integer personId) {
        return personRepository.findById(personId).map(person -> {
            personRepository.delete(person);
            return person;
        });
    }

    public Person update(Person person) {
        return personRepository.save(person);
    }
}

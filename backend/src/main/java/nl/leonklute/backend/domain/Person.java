package nl.leonklute.backend.domain;

public record Person(String name) {
    static Person fromString(String line) {
        return new Person(line);
    }
}
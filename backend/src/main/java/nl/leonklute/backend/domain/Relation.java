package nl.leonklute.backend.domain;

public record Relation(Person person1, Person person2, String relation) {
    static Relation fromString(String line) {
        String[] parts = line.split(",\s*");
        if (parts[0].compareTo(parts[1]) <= 0) {
            return new Relation(new Person(parts[0]), new Person(parts[1]), parts[2]);
        } else {
            return new Relation(new Person(parts[1]), new Person(parts[0]), parts[2]);
        }
    }
}
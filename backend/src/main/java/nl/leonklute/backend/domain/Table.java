package nl.leonklute.backend.domain;

public record Table(int target, int capacity) {
    static Table fromString(String line) {
        String[] parts = line.split(",\s*");
        return new Table(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}
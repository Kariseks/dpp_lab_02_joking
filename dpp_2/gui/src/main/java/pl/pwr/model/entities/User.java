package pl.pwr.model.entities;

public record User(
        int id,
        String username,
        String passwordHash,
        String creationDate
) {}

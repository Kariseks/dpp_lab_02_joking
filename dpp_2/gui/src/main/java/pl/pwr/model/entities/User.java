package pl.pwr.model.entities;

import java.time.LocalDate;

public record User(
        int id,
        String username,
        String passwordHash,
        LocalDate creationDate
) {}

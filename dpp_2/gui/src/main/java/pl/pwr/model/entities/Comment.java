package pl.pwr.model.entities;

public record Comment(
        int id,
        String text,
        int rating,
        int jokeId,
        int userId
) {}

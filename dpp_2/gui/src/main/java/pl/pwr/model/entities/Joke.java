package pl.pwr.model.entities;

public record Joke(
        int id,
        String title,
        String content,
        String author,
        String tags,
        String creationDate,
        int status,
        double averageRating // Dla SQL-owego AVG()
) {
    // Konstruktor pomocniczy dla nowych żartów (bez ocen)
    public Joke(int id, String title, String content, String author, String tags, String creationDate, int status) {
        this(id, title, content, author, tags, creationDate, status, 0.0);
    }
}

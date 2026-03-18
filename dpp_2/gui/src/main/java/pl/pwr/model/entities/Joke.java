package pl.pwr.model.entities;

/*
--STATUS
-- 1 nowo dodany / edytownay
-- 2 zgloszony
-- 3 zatwierdzony
-- 4 zawiesozny
*/

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

    // 1. Definicja Enuma wewnątrz rekordu
    public enum Status {
        NEW(0),
        REPORTED(1),
        ACCEPTED(2),
        BLOCKED(3);

        private final int value;
        Status(int value) { this.value = value;}
        public int getValue() {return value;}

        public static Status fromInt(int i) {
            for (Status s : Status.values()) {
                if (s.value == i) return s;
            }
            return NEW;
        }
    }

    public Joke(int id, String title, String content, String author, String tags, String creationDate, int status) {
        this(id, title, content, author, tags, creationDate, status, 0.0);
    }
}

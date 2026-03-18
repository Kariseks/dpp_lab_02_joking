package pl.pwr.model.DAO;

import pl.pwr.model.entities.Comment;
import pl.pwr.model.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public enum SortOrder { date_up, date_down, rate_up, rate_down }

    public enum DateFilter {
        days7(7), days30(30), days90(90);
        public final int days;
        DateFilter(int days) { this.days = days; }
    }

    public void addComment(Comment comment) {
        String sql = "INSERT INTO comments (text, rating, joke_id, user_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, comment.text());
            pstmt.setInt(2, comment.rating());
            pstmt.setInt(3, comment.jokeId());
            pstmt.setInt(4, comment.userId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Błąd zapisu komentarza", e);
        }
    }

    public List<Comment> getCommentsForJoke(int jokeId, DateFilter daysLimit, SortOrder order, Integer rateFilter) {
        List<Comment> comments = new ArrayList<>();
        // Uwaga: Twoja tabela comments nie ma kolumny creationDate. Sortujemy po ID (chronologicznie).
        StringBuilder sql = new StringBuilder("SELECT id, text, rating, joke_id, user_id FROM comments WHERE joke_id = ?");

        if (daysLimit != null) sql.append(" AND id > 0 "); // Symulacja filtra daty lub dodaj kolumnę do DB
        if (rateFilter != null) sql.append(" AND rating >= ?");

        if (order != null) {
            switch (order) {
                case date_up -> sql.append(" ORDER BY id ASC");
                case date_down -> sql.append(" ORDER BY id DESC");
                case rate_up -> sql.append(" ORDER BY rating ASC");
                case rate_down -> sql.append(" ORDER BY rating DESC");
            }
        }

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            //petla z licznikiem zamiast na sztywno i if-y
            int currentParam = 1;
            pstmt.setInt(currentParam++, jokeId);
            // fix zlego if-a: dynamiczne ustawianie parametrów - to musi być w tej samej kolejności co w SQL!
            if (rateFilter != null) {
                pstmt.setInt(currentParam++, rateFilter);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(new Comment(
                            rs.getInt("id"), rs.getString("text"),
                            rs.getInt("rating"), rs.getInt("joke_id"),
                            rs.getInt("user_id")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Błąd pobierania komentarzy", e);
        }
        return comments;
    }

    public double getAverageRating(int jokeId) {
        String sql = "SELECT COALESCE(AVG(rating), 0.0) FROM comments WHERE joke_id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jokeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Błąd obliczania średniej", e);
        }
        return 0.0;
    }
}
package pl.pwr.model.DAO;

import pl.pwr.model.entities.Comment;
import pl.pwr.model.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CommentDAO {

    public enum SortOrder {
        date_up,
        date_down,
        rate_up,
        rate_down;
    }

    public enum DateFilter {
        days7(7),
        days30(30),
        days90(90);

        public final int days;

        DateFilter(int days) {
            this.days = days;
        }
    }

    public void addComment(Comment comment) {

        String sql = "INSERT INTO comments (text, rating, joke_id, user_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Ustawiamy parametry w kolejności występowania pytajników
            pstmt.setString(1, comment.text());
            pstmt.setInt(2, comment.rating());
            pstmt.setInt(3, comment.jokeId());
            pstmt.setInt(4, comment.userId());

            int affectedRows = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Błąd podczas dodawania komentarza: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Comment> getCommentsForJoke(int jokeId, DateFilter daysLimit, SortOrder order, Integer rateFilter) {
        List<Comment> comments = new ArrayList<>();

        // Używamy StringBuilder, bo filtr daty jest opcjonalny
        StringBuilder sql = new StringBuilder("""
        SELECT id, text, rating, joke_id, user_id 
        FROM comments 
        WHERE joke_id = ?
    """);

        //filter
        if (daysLimit != null) {
            sql.append(" AND creationDate >= date('now', '-' || ? || ' days') ");
        }
        if (rateFilter != null) {
            sql.append(" AND rate >= ?");
        }

        //Sortowanie
        switch (order) {
            case date_up:   sql.append(" ORDER BY date ASC");break;
            case date_down: sql.append(" ORDER BY date DSC");break;
            case rate_up:   sql.append(" ORDER BY rate ASC");break;
            case rate_down: sql.append(" ORDER BY rate DSC");break;
        }


        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            pstmt.setInt(1, jokeId);

            //date filter
            if (daysLimit != null) {
                pstmt.setInt(2, daysLimit.days);
            }
            if (rateFilter != null) {
                pstmt.setInt(3, rateFilter);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(new Comment(
                            rs.getInt("id"),
                            rs.getString("text"),
                            rs.getInt("rating"),
                            rs.getInt("joke_id"),
                            rs.getInt("user_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    // OBLICZANIE ŚREDNIEJ OCEN W BAZIE DANYCH
    public double getAverageRating(int jokeId) {
        // COALESCE zabezpiecza nas przed NULL, jeśli żart nie ma jeszcze ocen
        String sql = "SELECT COALESCE(AVG(rating), 0.0) FROM comments WHERE joke_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, jokeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}


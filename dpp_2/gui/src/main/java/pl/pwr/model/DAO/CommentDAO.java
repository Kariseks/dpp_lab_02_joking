package pl.pwr.model.DAO;

import pl.pwr.model.DBConnector;
import pl.pwr.model.entities.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommentDAO {

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
            throw new RuntimeException("Błąd podczas dodawania komentarza do bazy danych: " + e.getMessage(), e);
        }
    }
}

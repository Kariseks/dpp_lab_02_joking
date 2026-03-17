package pl.pwr.model.DAO;

import pl.pwr.model.DBConnector;
import pl.pwr.model.entities.Joke;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JokeDao {

    public List<Joke> findJokes(String tagFilter, String titleSearch, String sortBy) {
        List<Joke> jokes = new ArrayList<>();

        // Podstawowe zapytanie z GROUP_CONCAT, żeby zwinąć tagi do jednego pola
        StringBuilder sql = new StringBuilder("""
            SELECT j.id, j.title, j.content, j.creationDate, j.status, u.username, 
                   GROUP_CONCAT(t.name, ', ') as tag_list,
                   (SELECT AVG(rating) FROM comments WHERE joke_id = j.id) as avg_rating
            FROM jokes j
            JOIN users u ON j.user_id = u.id
            LEFT JOIN joke_tags jt ON j.id = jt.joke_id
            LEFT JOIN tags t ON jt.tag_id = t.id
            WHERE 1=1
        """);

        // Dynamiczne filtry
        if (titleSearch != null && !titleSearch.isEmpty()) sql.append(" AND j.title LIKE ? ");
        if (tagFilter != null && !tagFilter.isEmpty()) {
            sql.append(" AND j.id IN (SELECT joke_id FROM joke_tags jt2 JOIN tags t2 ON jt2.tag_id = t2.id WHERE t2.name = ?) ");
        }

        sql.append(" GROUP BY j.id ");

        // Sortowanie
        if (sortBy != null) {
            switch (sortBy) {
                case "DATE" -> sql.append(" ORDER BY j.creationDate DESC ");
                case "RATING" -> sql.append(" ORDER BY avg_rating DESC ");
                case "USER" -> sql.append(" ORDER BY u.username ASC ");
                default -> sql.append(" ORDER BY j.id DESC ");
            }
        }

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIdx = 1;
            if (titleSearch != null && !titleSearch.isEmpty()) pstmt.setString(paramIdx++, "%" + titleSearch + "%");
            if (tagFilter != null && !tagFilter.isEmpty()) pstmt.setString(paramIdx++, tagFilter);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jokes.add(new Joke(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("author"),
                        rs.getString("tag_list"),
                        rs.getString("creationDate"),
                        rs.getInt("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return jokes;
    }

    // 2. DODAWANIE ŻARTU Z TAGAMI (Transakcyjne)
    public void addJoke(String title, String content, int userId, List<Integer> tagIds) {
        String sqlJoke = "INSERT INTO jokes (title, content, user_id, creationDate, status, displayCount) VALUES (?, ?, ?, date('now'), 1, 0)";
        String sqlTag = "INSERT INTO joke_tags (joke_id, tag_id) VALUES (?, ?)";

        try (Connection conn = DBConnector.getConnection()) {
            conn.setAutoCommit(false); // Start transakcji

            try (PreparedStatement psJoke = conn.prepareStatement(sqlJoke, Statement.RETURN_GENERATED_KEYS)) {
                psJoke.setString(1, title);
                psJoke.setString(2, content);
                psJoke.setInt(3, userId);
                psJoke.executeUpdate();

                ResultSet keys = psJoke.getGeneratedKeys();
                if (keys.next()) {
                    int jokeId = keys.getInt(1);
                    try (PreparedStatement psTag = conn.prepareStatement(sqlTag)) {
                        for (Integer tId : tagIds) {
                            psTag.setInt(1, jokeId);
                            psTag.setInt(2, tId);
                            psTag.addBatch();
                        }
                        psTag.executeBatch();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // 3. ZMIANA STATUSU (np. Akceptacja/Ukrycie)
    public void updateStatus(int jokeId, int newStatus) {
        String sql = "UPDATE jokes SET status = ? WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newStatus);
            pstmt.setInt(2, jokeId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }


    public void delete(int id) {
        String sql = "DELETE FROM jokes WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
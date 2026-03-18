package pl.pwr.model.DAO;

import pl.pwr.model.DBConnector;
import pl.pwr.model.entities.Tag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {

    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT id, name FROM tags ORDER BY name ";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                tags.add(new Tag(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania tagów: " + e.getMessage());
        }

        return tags;
    }


}

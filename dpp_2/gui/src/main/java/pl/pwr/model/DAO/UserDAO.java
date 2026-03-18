package pl.pwr.model.DAO;

import pl.pwr.model.DBConnector;
import pl.pwr.model.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDAO {

    public String getUsernameById(int id) {
        String sql = "SELECT username FROM users WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;//there is no user with given id
    }


    public User getUser(String username) {
        String sql = "SELECT id, passwordHash, creationDate FROM users WHERE username = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String passwordHash = rs.getString("passwordHash");
                    String rawDate = rs.getString("creationDate");
                    LocalDate date = (rawDate != null) ? LocalDate.parse(rawDate) : LocalDate.now();

                    return new User(id,username,passwordHash,date);
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
}

package se.sprinto.hakan.chatapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import se.sprinto.hakan.chatapp.DatabaseUtil;
import se.sprinto.hakan.chatapp.model.User;

public class UserDatabaseDAO implements UserDAO {

    @Override
    public User register(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    user.setId(id);
                }
            }
            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Error registering user", e);
        }
    }

    @Override
    public User login(String username, String password) {
        String sql = "SELECT id, username, password FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    return user;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error logging in", e);
        }

        return null;
    }

}

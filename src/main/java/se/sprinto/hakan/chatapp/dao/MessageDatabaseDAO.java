package se.sprinto.hakan.chatapp.dao;

import se.sprinto.hakan.chatapp.DatabaseUtil;
import se.sprinto.hakan.chatapp.model.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDatabaseDAO implements MessageDAO {

    @Override
    public void saveMessage(Message message) {
        String sql = "INSERT INTO messages (user_id, content, created_at) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, message.getUserId());
            stmt.setString(2, message.getText());
            stmt.setTimestamp(3, Timestamp.valueOf(message.getTimestamp()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving message", e);
        }
    }

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        String sql = "SELECT user_id, content, created_at FROM messages WHERE user_id = ? ORDER BY created_at";

        List<Message> messages = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    int uid = rs.getInt("user_id");
                    String text = rs.getString("content");
                    LocalDateTime timestamp = rs.getTimestamp("created_at").toLocalDateTime();

                    Message msg = new Message(uid, text, timestamp);

                    messages.add(msg);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching messages", e);
        }

        return messages;
    }
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DBUtil;

public class GroupIdDAO {

    public boolean findByGroupId(String groupId) {
        String sql = "SELECT group_id FROM public.group_id WHERE group_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertGroupId(String groupId, int createdBy) {
        String sql = "INSERT INTO public.group_id (group_id, created_by) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, groupId);
            ps.setInt(2, createdBy);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authenticateGroup(String groupId, String creatorEmail) {
        String sql = "SELECT g.group_id FROM public.group_id g " +
                     "JOIN public.\"USER\" u ON g.created_by = u.user_id " +
                     "WHERE g.group_id = ? AND u.mail_address = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, groupId);
            ps.setString(2, creatorEmail);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

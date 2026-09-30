package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.DiaryBean;
import util.DBUtil;

public class DiaryDAO {

    static {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE public.\"日記\" ADD COLUMN IF NOT EXISTS mode boolean DEFAULT false");
        } catch (Exception e) {
            // Ignore DB schema auto-migration errors if DB is unreachable during initialization
        }
    }

    public DiaryBean findByDateAndUser(int userId, Date date) {
        return findByDateAndUser(userId, date, false);
    }

    public DiaryBean findByDateAndUser(int userId, Date date, boolean mode) {
        String sql = "SELECT * FROM public.\"日記\" WHERE user_id = ? AND date = ? AND mode = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, date);
            ps.setBoolean(3, mode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DiaryBean d = new DiaryBean();
                    d.setId(rs.getInt("id"));
                    d.setUserId(rs.getInt("user_id"));
                    d.setDate(rs.getDate("date"));
                    d.setPhoto(rs.getBytes("photo"));
                    d.setContent(rs.getString("content"));
                    d.setMode(rs.getBoolean("mode"));
                    return d;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveOrUpdate(DiaryBean diary) {
        DiaryBean existing = findByDateAndUser(diary.getUserId(), diary.getDate(), diary.isMode());
        if (existing == null) {
            String sql = "INSERT INTO public.\"日記\" (user_id, date, photo, content, mode) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, diary.getUserId());
                ps.setDate(2, diary.getDate());
                ps.setBytes(3, diary.getPhoto());
                ps.setString(4, diary.getContent());
                ps.setBoolean(5, diary.isMode());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String sql = "UPDATE public.\"日記\" SET photo = ?, content = ? WHERE id = ? AND user_id = ? AND mode = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBytes(1, diary.getPhoto());
                ps.setString(2, diary.getContent());
                ps.setInt(3, existing.getId());
                ps.setInt(4, diary.getUserId());
                ps.setBoolean(5, diary.isMode());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.UserBean;
import util.DBUtil;

public class UserDAO {

    public UserBean findByMailAddress(String mailAddress) {
        String sql = "SELECT * FROM public.\"USER\" WHERE mail_address = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mailAddress);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserBean u = new UserBean();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUserIcon(rs.getBytes("user_icon"));
                    u.setUserName(rs.getString("user_name"));
                    u.setMailAddress(rs.getString("mail_address"));
                    u.setPassword(rs.getString("password"));
                    u.setSecretQuestion(rs.getString("secret_question"));
                    u.setSecretAnswer(rs.getString("secret_answer"));
                    u.setNickname(rs.getString("nickname"));
                    u.setGroupId(rs.getString("group_id"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserBean findByUserId(int userId) {
        String sql = "SELECT * FROM public.\"USER\" WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserBean u = new UserBean();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUserIcon(rs.getBytes("user_icon"));
                    u.setUserName(rs.getString("user_name"));
                    u.setMailAddress(rs.getString("mail_address"));
                    u.setPassword(rs.getString("password"));
                    u.setSecretQuestion(rs.getString("secret_question"));
                    u.setSecretAnswer(rs.getString("secret_answer"));
                    u.setNickname(rs.getString("nickname"));
                    u.setGroupId(rs.getString("group_id"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertUser(UserBean user) {
        String sql = "INSERT INTO public.\"USER\" (user_icon, user_name, mail_address, password, secret_question, secret_answer, nickname, create_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, user.getUserIcon());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getMailAddress());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getSecretQuestion());
            ps.setString(6, user.getSecretAnswer());
            ps.setString(7, user.getNickname());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(UserBean user) {
        String sql = "UPDATE public.\"USER\" SET user_icon = ?, user_name = ?, mail_address = ?, password = ?, secret_question = ?, secret_answer = ?, nickname = ? WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, user.getUserIcon());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getMailAddress());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getSecretQuestion());
            ps.setString(6, user.getSecretAnswer());
            ps.setString(7, user.getNickname());
            ps.setInt(8, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateGroupId(int userId, String groupId) {
        String sql = "UPDATE public.\"USER\" SET group_id = ? WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, groupId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UserBean> findByGroupId(String groupId) {
        List<UserBean> list = new ArrayList<>();
        if (groupId == null || groupId.trim().isEmpty()) {
            return list;
        }
        String sql = "SELECT * FROM public.\"USER\" WHERE group_id = ? ORDER BY user_id ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserBean u = new UserBean();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUserIcon(rs.getBytes("user_icon"));
                    u.setUserName(rs.getString("user_name"));
                    u.setMailAddress(rs.getString("mail_address"));
                    u.setPassword(rs.getString("password"));
                    u.setSecretQuestion(rs.getString("secret_question"));
                    u.setSecretAnswer(rs.getString("secret_answer"));
                    u.setNickname(rs.getString("nickname"));
                    u.setGroupId(rs.getString("group_id"));
                    list.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

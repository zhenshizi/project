package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.TodoBean;
import util.DBUtil;

public class TodoDAO {

    public List<TodoBean> findByUserId(int userId) {
        List<TodoBean> list = new ArrayList<>();
        String sql = "SELECT * FROM public.todo WHERE user_id = ? ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TodoBean t = new TodoBean();
                    t.setId(rs.getInt("id"));
                    t.setUserId(rs.getInt("user_id"));
                    t.setTodo(rs.getString("todo"));
                    t.setIdea(rs.getString("idea"));
                    t.setStatus(rs.getBoolean("status"));
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertTodo(TodoBean todo) {
        String sql = "INSERT INTO public.todo (user_id, todo, idea, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, todo.getUserId());
            ps.setString(2, todo.getTodo());
            ps.setString(3, todo.getIdea());
            ps.setBoolean(4, todo.isStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTodoText(int id, int userId, String todoText) {
        String sql = "UPDATE public.todo SET todo = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, todoText);
            ps.setInt(2, id);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTodoStatus(int id, int userId, boolean status) {
        String sql = "UPDATE public.todo SET status = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateIdea(int userId, String idea) {
        String checkSql = "SELECT id FROM public.todo WHERE user_id = ? AND idea IS NOT NULL LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String updateSql = "UPDATE public.todo SET idea = ? WHERE id = ? AND user_id = ?";
                    try (PreparedStatement ups = conn.prepareStatement(updateSql)) {
                        ups.setString(1, idea);
                        ups.setInt(2, id);
                        ups.setInt(3, userId);
                        return ups.executeUpdate() > 0;
                    }
                } else {
                    String insertSql = "INSERT INTO public.todo (user_id, idea, status) VALUES (?, ?, false)";
                    try (PreparedStatement ips = conn.prepareStatement(insertSql)) {
                        ips.setInt(1, userId);
                        ips.setString(2, idea);
                        return ips.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTodo(int id, int userId) {
        String sql = "DELETE FROM public.todo WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

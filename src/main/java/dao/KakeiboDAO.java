package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KakeiboBean;
import util.DBUtil;

public class KakeiboDAO {

    public boolean insertKakeibo(KakeiboBean kakeibo) {
        String sql = "INSERT INTO public.\"家計簿入力\" (user_id, date, memo, yen, category_type, mode, himoku_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kakeibo.getUserId());
            ps.setDate(2, kakeibo.getDate());
            ps.setString(3, kakeibo.getMemo());
            ps.setBigDecimal(4, kakeibo.getYen());
            ps.setString(5, kakeibo.getCategoryType());
            ps.setBoolean(6, kakeibo.isMode());
            ps.setInt(7, kakeibo.getHimokuId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateKakeibo(KakeiboBean kakeibo) {
        String sql = "UPDATE public.\"家計簿入力\" SET date = ?, memo = ?, yen = ?, category_type = ?, mode = ?, himoku_id = ? " +
                     "WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, kakeibo.getDate());
            ps.setString(2, kakeibo.getMemo());
            ps.setBigDecimal(3, kakeibo.getYen());
            ps.setString(4, kakeibo.getCategoryType());
            ps.setBoolean(5, kakeibo.isMode());
            ps.setInt(6, kakeibo.getHimokuId());
            ps.setInt(7, kakeibo.getId());
            ps.setInt(8, kakeibo.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteKakeibo(int id, int userId) {
        String sql = "DELETE FROM public.\"家計簿入力\" WHERE id = ? AND user_id = ?";
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

    public KakeiboBean findById(int id) {
        String sql = "SELECT k.*, h.category_name FROM public.\"家計簿入力\" k " +
                     "LEFT JOIN public.\"費目\" h ON k.himoku_id = h.himoku_id " +
                     "WHERE k.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KakeiboBean k = new KakeiboBean();
                    k.setId(rs.getInt("id"));
                    k.setUserId(rs.getInt("user_id"));
                    k.setDate(rs.getDate("date"));
                    k.setMemo(rs.getString("memo"));
                    k.setYen(rs.getBigDecimal("yen"));
                    k.setCategoryType(rs.getString("category_type"));
                    k.setMode(rs.getBoolean("mode"));
                    k.setHimokuId(rs.getInt("himoku_id"));
                    k.setCategoryName(rs.getString("category_name"));
                    return k;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countSharedData(int userId) {
        String sql = "SELECT COUNT(*) FROM public.\"家計簿入力\" WHERE user_id = ? AND mode = true";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<KakeiboBean> findDailyPrivate(int userId, Date date) {
        List<KakeiboBean> list = new ArrayList<>();
        String sql = "SELECT k.*, h.category_name FROM public.\"家計簿入力\" k " +
                     "LEFT JOIN public.\"費目\" h ON k.himoku_id = h.himoku_id " +
                     "WHERE k.user_id = ? AND k.date = ? AND k.mode = false ORDER BY k.id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KakeiboBean k = new KakeiboBean();
                    k.setId(rs.getInt("id"));
                    k.setUserId(rs.getInt("user_id"));
                    k.setDate(rs.getDate("date"));
                    k.setMemo(rs.getString("memo"));
                    k.setYen(rs.getBigDecimal("yen"));
                    k.setCategoryType(rs.getString("category_type"));
                    k.setMode(rs.getBoolean("mode"));
                    k.setHimokuId(rs.getInt("himoku_id"));
                    k.setCategoryName(rs.getString("category_name"));
                    list.add(k);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KakeiboBean> findDailyShared(int targetUserId, Date date) {
        List<KakeiboBean> list = new ArrayList<>();
        String sql = "SELECT k.*, h.category_name FROM public.\"家計簿入力\" k " +
                     "LEFT JOIN public.\"費目\" h ON k.himoku_id = h.himoku_id " +
                     "WHERE k.user_id = ? AND k.date = ? AND k.mode = true ORDER BY k.id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, targetUserId);
            ps.setDate(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KakeiboBean k = new KakeiboBean();
                    k.setId(rs.getInt("id"));
                    k.setUserId(rs.getInt("user_id"));
                    k.setDate(rs.getDate("date"));
                    k.setMemo(rs.getString("memo"));
                    k.setYen(rs.getBigDecimal("yen"));
                    k.setCategoryType(rs.getString("category_type"));
                    k.setMode(rs.getBoolean("mode"));
                    k.setHimokuId(rs.getInt("himoku_id"));
                    k.setCategoryName(rs.getString("category_name"));
                    list.add(k);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KakeiboBean> findMonthlyPrivate(int userId, String yearMonth) { // yearMonth: YYYY-MM
        List<KakeiboBean> list = new ArrayList<>();
        String sql = "SELECT k.*, h.category_name FROM public.\"家計簿入力\" k " +
                     "LEFT JOIN public.\"費目\" h ON k.himoku_id = h.himoku_id " +
                     "WHERE k.user_id = ? AND to_char(k.date, 'YYYY-MM') = ? AND k.mode = false";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, yearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KakeiboBean k = new KakeiboBean();
                    k.setId(rs.getInt("id"));
                    k.setUserId(rs.getInt("user_id"));
                    k.setDate(rs.getDate("date"));
                    k.setMemo(rs.getString("memo"));
                    k.setYen(rs.getBigDecimal("yen"));
                    k.setCategoryType(rs.getString("category_type"));
                    k.setMode(rs.getBoolean("mode"));
                    k.setHimokuId(rs.getInt("himoku_id"));
                    k.setCategoryName(rs.getString("category_name"));
                    list.add(k);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KakeiboBean> findMonthlyShared(int targetUserId, String yearMonth) {
        List<KakeiboBean> list = new ArrayList<>();
        String sql = "SELECT k.*, h.category_name FROM public.\"家計簿入力\" k " +
                     "LEFT JOIN public.\"費目\" h ON k.himoku_id = h.himoku_id " +
                     "WHERE k.user_id = ? AND to_char(k.date, 'YYYY-MM') = ? AND k.mode = true";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, targetUserId);
            ps.setString(2, yearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KakeiboBean k = new KakeiboBean();
                    k.setId(rs.getInt("id"));
                    k.setUserId(rs.getInt("user_id"));
                    k.setDate(rs.getDate("date"));
                    k.setMemo(rs.getString("memo"));
                    k.setYen(rs.getBigDecimal("yen"));
                    k.setCategoryType(rs.getString("category_type"));
                    k.setMode(rs.getBoolean("mode"));
                    k.setHimokuId(rs.getInt("himoku_id"));
                    k.setCategoryName(rs.getString("category_name"));
                    list.add(k);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KakeiboBean> findYearlyPrivate(int userId, String year) { // year: YYYY
        List<KakeiboBean> list = new ArrayList<>();
        String sql = "SELECT k.*, h.category_name FROM public.\"家計簿入力\" k " +
                     "LEFT JOIN public.\"費目\" h ON k.himoku_id = h.himoku_id " +
                     "WHERE k.user_id = ? AND to_char(k.date, 'YYYY') = ? AND k.mode = false";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KakeiboBean k = new KakeiboBean();
                    k.setId(rs.getInt("id"));
                    k.setUserId(rs.getInt("user_id"));
                    k.setDate(rs.getDate("date"));
                    k.setMemo(rs.getString("memo"));
                    k.setYen(rs.getBigDecimal("yen"));
                    k.setCategoryType(rs.getString("category_type"));
                    k.setMode(rs.getBoolean("mode"));
                    k.setHimokuId(rs.getInt("himoku_id"));
                    k.setCategoryName(rs.getString("category_name"));
                    list.add(k);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KakeiboBean> findYearlyShared(int targetUserId, String year) {
        List<KakeiboBean> list = new ArrayList<>();
        String sql = "SELECT k.*, h.category_name FROM public.\"家計簿入力\" k " +
                     "LEFT JOIN public.\"費目\" h ON k.himoku_id = h.himoku_id " +
                     "WHERE k.user_id = ? AND to_char(k.date, 'YYYY') = ? AND k.mode = true";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, targetUserId);
            ps.setString(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KakeiboBean k = new KakeiboBean();
                    k.setId(rs.getInt("id"));
                    k.setUserId(rs.getInt("user_id"));
                    k.setDate(rs.getDate("date"));
                    k.setMemo(rs.getString("memo"));
                    k.setYen(rs.getBigDecimal("yen"));
                    k.setCategoryType(rs.getString("category_type"));
                    k.setMode(rs.getBoolean("mode"));
                    k.setHimokuId(rs.getInt("himoku_id"));
                    k.setCategoryName(rs.getString("category_name"));
                    list.add(k);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

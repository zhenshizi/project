package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.KakeiboBean;
import model.UserBean;
import dao.KakeiboDAO;

@WebServlet("/input")
public class InputServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String editIdStr = request.getParameter("id");
        KakeiboBean kakeibo = null;

        if (editIdStr != null && !editIdStr.trim().isEmpty()) {
            try {
                int editId = Integer.parseInt(editIdStr);
                KakeiboDAO dao = new KakeiboDAO();
                kakeibo = dao.findById(editId);
                if (kakeibo != null) {
                    request.setAttribute("kakeibo", kakeibo);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 日付の決定優先順位: 
        // 1. 編集対象データの如果日付 (kakeibo.date)
        // 2. リクエストパラメータで渡ってきた日付 (date)
        // 3. どちらもなければ本日の日付
        String todayStr = java.time.LocalDate.now().toString();
        String dateParam = request.getParameter("date");
        String finalDate = todayStr;

        if (kakeibo != null && kakeibo.getDate() != null) {
            finalDate = kakeibo.getDate().toString();
        } else if (dateParam != null && dateParam.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            finalDate = dateParam;
        }

        // JSP側が期待している "dateStr" に統一してセット
        request.setAttribute("dateStr", finalDate);

        request.getRequestDispatcher("/WEB-INF/jsp/input.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        String idStr = request.getParameter("id");
        String dateStr = request.getParameter("date");
        String memo = request.getParameter("memo");
        String yenStr = request.getParameter("yen");
        String modeStr = request.getParameter("mode"); // "false":個人, "true":共有
        String himokuIdStr = request.getParameter("himoku_id");

        boolean hasError = false;
        String popError = null;

        if (yenStr == null || yenStr.trim().isEmpty()) {
            popError = "金額を入力してください。";
            hasError = true;
        } else {
            try {
                BigDecimal yenVal = new BigDecimal(yenStr.trim());
                if (yenVal.compareTo(BigDecimal.ZERO) <= 0) {
                    popError = "金額が不正です。";
                    hasError = true;
                }
            } catch (Exception e) {
                popError = "金額が不正です。";
                hasError = true;
            }
        }

        if (!hasError && (modeStr == null || modeStr.trim().isEmpty())) {
            popError = "カテゴリを選択してください。";
            hasError = true;
        }

        if (!hasError && (himokuIdStr == null || himokuIdStr.trim().isEmpty())) {
            popError = "費目を選択してください。";
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("popError", popError);
            request.setAttribute("dateStr", dateStr);
            request.setAttribute("memo", memo);
            request.setAttribute("yenStr", yenStr);
            request.setAttribute("modeStr", modeStr);
            request.setAttribute("himokuIdStr", himokuIdStr);
            if (idStr != null && !idStr.trim().isEmpty()) {
                request.setAttribute("editId", idStr);
            }
            request.getRequestDispatcher("/WEB-INF/jsp/input.jsp").forward(request, response);
            return;
        }

        int himokuId = Integer.parseInt(himokuIdStr.trim());
        String categoryType = "2"; // デフォルト変動
        if (himokuId == 0 || himokuId == 1) {
            categoryType = "0"; // 収入
        } else if (himokuId >= 10 && himokuId <= 17) {
            categoryType = "1"; // 固定
        } else if (himokuId >= 30 && himokuId <= 39) {
            categoryType = "2"; // 変動
        } else if (himokuId == 50 || himokuId == 51) {
            categoryType = "3"; // 投資・貯蓄
        }

        boolean mode = "true".equalsIgnoreCase(modeStr);
        Date date = Date.valueOf(dateStr);
        BigDecimal yen = new BigDecimal(yenStr.trim());

        KakeiboBean kakeibo = new KakeiboBean();
        kakeibo.setUserId(currentUser.getUserId());
        kakeibo.setDate(date);
        kakeibo.setMemo(memo);
        kakeibo.setYen(yen);
        kakeibo.setCategoryType(categoryType);
        kakeibo.setMode(mode);
        kakeibo.setHimokuId(himokuId);

        KakeiboDAO dao = new KakeiboDAO();
        if (idStr != null && !idStr.trim().isEmpty()) {
            kakeibo.setId(Integer.parseInt(idStr.trim()));
            dao.updateKakeibo(kakeibo);
        } else {
            dao.insertKakeibo(kakeibo);
        }

        // セッションスコープ更新機能(isShared)
        int sharedCount = dao.countSharedData(currentUser.getUserId());
        currentUser.setShare(sharedCount > 0);
        session.setAttribute("currentUser", currentUser);

        response.sendRedirect(request.getContextPath() + "/calendar-monthly-private");
    }
}
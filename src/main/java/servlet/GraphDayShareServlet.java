package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GraphCategoryBean;
import model.KakeiboBean;
import model.UserBean;
import dao.KakeiboDAO;
import dao.UserDAO;

@WebServlet("/graph-day-share")
public class GraphDayShareServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        if (!currentUser.isShare() && (currentUser.getGroupId() == null || currentUser.getGroupId().trim().isEmpty())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserDAO userDAO = new UserDAO();
        List<UserBean> groupMembers = userDAO.findByGroupId(currentUser.getGroupId());

        String targetUserIdStr = request.getParameter("target_user_id");
        Integer targetUserId = null;
        if (targetUserIdStr != null && !targetUserIdStr.trim().isEmpty() && !"all".equals(targetUserIdStr)) {
            try {
                targetUserId = Integer.parseInt(targetUserIdStr.trim());
            } catch (NumberFormatException e) {
                targetUserId = null;
            }
        }

        String dateStr = request.getParameter("date");
        LocalDate dateVal;
        if (dateStr != null && dateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            dateVal = LocalDate.parse(dateStr);
        } else {
            dateVal = LocalDate.now();
        }

        Date sqlDate = Date.valueOf(dateVal);

        KakeiboDAO dao = new KakeiboDAO();
        List<KakeiboBean> list = new ArrayList<>();
        if (targetUserId != null) {
            list = dao.findDailyShared(targetUserId, sqlDate);
        } else {
            for (UserBean u : groupMembers) {
                list.addAll(dao.findDailyShared(u.getUserId(), sqlDate));
            }
        }

        BigDecimal totalExpense = BigDecimal.ZERO;
        Map<Integer, BigDecimal> amountMap = new HashMap<>();
        Map<Integer, String> nameMap = new HashMap<>();

        for (KakeiboBean k : list) {
            if (!"0".equals(k.getCategoryType())) {
                totalExpense = totalExpense.add(k.getYen());
                int hId = k.getHimokuId();
                amountMap.put(hId, amountMap.getOrDefault(hId, BigDecimal.ZERO).add(k.getYen()));
                nameMap.put(hId, k.getCategoryName() != null ? k.getCategoryName() : "費目" + hId);
            }
        }

        List<GraphCategoryBean> catList = new ArrayList<>();
        if (totalExpense.compareTo(BigDecimal.ZERO) > 0) {
            for (Map.Entry<Integer, BigDecimal> entry : amountMap.entrySet()) {
                GraphCategoryBean gc = new GraphCategoryBean();
                gc.setHimokuId(entry.getKey());
                gc.setCategoryName(nameMap.get(entry.getKey()));
                gc.setAmount(entry.getValue());
                double pct = entry.getValue().multiply(new BigDecimal(100)).divide(totalExpense, 1, RoundingMode.HALF_UP).doubleValue();
                gc.setPercentage(pct);
                catList.add(gc);
            }
        }

        request.setAttribute("dateVal", dateVal);
        request.setAttribute("dateStr", dateVal.toString());
        request.setAttribute("prevDate", dateVal.minusDays(1).toString());
        request.setAttribute("nextDate", dateVal.plusDays(1).toString());
        request.setAttribute("totalExpense", totalExpense);
        request.setAttribute("catList", catList);
        request.setAttribute("groupMembers", groupMembers);
        request.setAttribute("targetUserIdStr", targetUserIdStr != null ? targetUserIdStr : "all");

        request.getRequestDispatcher("/WEB-INF/jsp/graph-day-share.jsp").forward(request, response);
    }
}

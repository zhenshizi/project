package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

@WebServlet("/graph-year-private")
public class GraphYearPrivateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        String yearStr = request.getParameter("date");
        if (yearStr == null || !yearStr.matches("^\\d{4}$")) {
            yearStr = String.valueOf(LocalDate.now().getYear());
        }

        KakeiboDAO dao = new KakeiboDAO();
        List<KakeiboBean> list = dao.findYearlyPrivate(currentUser.getUserId(), yearStr);

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

        int yearInt = Integer.parseInt(yearStr);
        request.setAttribute("yearStr", yearStr);
        request.setAttribute("prevYear", String.valueOf(yearInt - 1));
        request.setAttribute("nextYear", String.valueOf(yearInt + 1));
        request.setAttribute("totalExpense", totalExpense);
        request.setAttribute("catList", catList);

        request.getRequestDispatcher("/WEB-INF/jsp/graph-year-private.jsp").forward(request, response);
    }
}

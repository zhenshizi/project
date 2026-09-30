package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.DiaryDAO;
import dao.KakeiboDAO;
import dao.UserDAO;
import model.CalendarCellBean;
import model.DiaryBean;
import model.KakeiboBean;
import model.UserBean;

@WebServlet("/calendar-monthly-private")
public class CalendarMonthlyPrivateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        String yearMonthStr = request.getParameter("date");
        LocalDate now = LocalDate.now();
        YearMonth ym;
        if (yearMonthStr != null && yearMonthStr.matches("^\\d{4}-\\d{2}$")) {
            ym = YearMonth.parse(yearMonthStr);
        } else {
            ym = YearMonth.from(now);
        }

        String currentYMStr = ym.toString(); // YYYY-MM

        KakeiboDAO kakeiboDAO = new KakeiboDAO();
        DiaryDAO diaryDAO = new DiaryDAO();
        List<KakeiboBean> kakeiboList = kakeiboDAO.findMonthlyPrivate(currentUser.getUserId(), currentYMStr);

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal fixed = BigDecimal.ZERO;
        BigDecimal variable = BigDecimal.ZERO;
        BigDecimal invest = BigDecimal.ZERO;

        for (KakeiboBean k : kakeiboList) {
            String cat = k.getCategoryType();
            if ("0".equals(cat)) {
                income = income.add(k.getYen());
            } else if ("1".equals(cat)) {
                fixed = fixed.add(k.getYen());
            } else if ("2".equals(cat)) {
                variable = variable.add(k.getYen());
            } else if ("3".equals(cat)) {
                invest = invest.add(k.getYen());
            }
        }

        BigDecimal netResult = income.subtract(fixed).subtract(variable).subtract(invest);
        BigDecimal remainBudget = netResult;

        int daysInMonth = ym.lengthOfMonth();
        int remainingDays;
        if (ym.getYear() == now.getYear() && ym.getMonth() == now.getMonth()) {
            remainingDays = daysInMonth - now.getDayOfMonth() + 1;
        } else if (ym.isBefore(YearMonth.from(now))) {
            remainingDays = 1;
        } else {
            remainingDays = daysInMonth;
        }

        BigDecimal dailyBudget = BigDecimal.ZERO;
        if (remainingDays > 0) {
            dailyBudget = remainBudget.divide(new BigDecimal(remainingDays), 0, RoundingMode.FLOOR);
        }

        BigDecimal baseValue = BigDecimal.ZERO;
        BigDecimal netBase = income.subtract(fixed).subtract(invest);
        if (daysInMonth > 0 && netBase.compareTo(BigDecimal.ZERO) > 0) {
            baseValue = netBase.divide(new BigDecimal(daysInMonth), 2, RoundingMode.HALF_UP);
        }

        BigDecimal todayExpense = BigDecimal.ZERO;
        Date todaySqlDate = Date.valueOf(now);
        for (KakeiboBean k : kakeiboList) {
            if (k.getDate().equals(todaySqlDate) && !"0".equals(k.getCategoryType())) {
                todayExpense = todayExpense.add(k.getYen());
            }
        }

        double usageRate = 0.0;
        if (dailyBudget.compareTo(BigDecimal.ZERO) > 0) {
            usageRate = todayExpense.divide(dailyBudget, 4, RoundingMode.HALF_UP).doubleValue() * 100;
        }

        // ステータス画像の判定
        String catStatusImg;
                
        // 日割り予算または残り予算が0以下の場合は無条件で「赤字（猫背）」にする
        if (dailyBudget.compareTo(BigDecimal.ZERO) <= 0 || remainBudget.compareTo(BigDecimal.ZERO) < 0) {
            catStatusImg = "nekoze.gif";      // 予算オーバー
        } else if (usageRate < 70.0) {
            catStatusImg = "nesoberi.gif";    // 良好（70%未満）
        } else if (usageRate <= 100.0) {
            catStatusImg = "siseiyoi.gif";    // 注意（70%〜100%）
        } else {
            catStatusImg = "nekoze.gif";      // 当日予算オーバー
        }

        UserDAO userDAO = new UserDAO();
        UserBean latestUser = userDAO.findByUserId(currentUser.getUserId());
        byte[] userIcon = (latestUser != null) ? latestUser.getUserIcon() : currentUser.getUserIcon();
        String pastelColor = getPastelColorFromUserIcon(userIcon);

        DayOfWeek firstDayOfWeek = ym.atDay(1).getDayOfWeek(); // MONDAY=1,...,SUNDAY=7
        int startOffset = firstDayOfWeek.getValue() - 1;

        List<CalendarCellBean> cells = new ArrayList<>();
        for (int i = 0; i < startOffset; i++) {
            CalendarCellBean cell = new CalendarCellBean();
            cell.setCurrentMonth(false);
            cells.add(cell);
        }

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate dateVal = ym.atDay(day);
            Date sqlDate = Date.valueOf(dateVal);
            String dateStr = dateVal.toString();

            boolean hasKakeibo = false;
            for (KakeiboBean k : kakeiboList) {
                if (k.getDate().equals(sqlDate)) {
                    hasKakeibo = true;
                    break;
                }
            }

            DiaryBean diary = diaryDAO.findByDateAndUser(currentUser.getUserId(), sqlDate, false);
            boolean hasDiary = (diary != null && ((diary.getContent() != null && !diary.getContent().trim().isEmpty()) || (diary.getPhoto() != null && diary.getPhoto().length > 0)));

            CalendarCellBean cell = new CalendarCellBean();
            cell.setDay(day);
            cell.setDateStr(dateStr);
            cell.setCurrentMonth(true);
            cell.setHasData(hasKakeibo || hasDiary);
            cell.setUserIconColor(pastelColor);
            cells.add(cell);
        }

        while (cells.size() % 7 != 0) {
            CalendarCellBean cell = new CalendarCellBean();
            cell.setCurrentMonth(false);
            cells.add(cell);
        }

        request.setAttribute("yearMonthStr", currentYMStr);
        request.setAttribute("prevYM", ym.minusMonths(1).toString());
        request.setAttribute("nextYM", ym.plusMonths(1).toString());
        request.setAttribute("income", income);
        request.setAttribute("fixed", fixed);
        request.setAttribute("variable", variable);
        request.setAttribute("invest", invest);
        request.setAttribute("netResult", netResult);
        request.setAttribute("remainBudget", remainBudget);
        request.setAttribute("remainingDays", remainingDays);
        request.setAttribute("dailyBudget", dailyBudget);
        request.setAttribute("catStatusImg", catStatusImg);
        request.setAttribute("cells", cells);

        request.getRequestDispatcher("/WEB-INF/jsp/calendar-monthly-private.jsp").forward(request, response);
    }

    private String getPastelColorFromUserIcon(byte[] userIcon) {
        if (userIcon == null || userIcon.length == 0) {
            return "#bae6fd";
        }
        int len = userIcon.length;
        switch (len) {
            case 25841: return "#fde68a"; // yellow
            case 30328: return "#bbf7d0"; // green
            case 30685: return "#c7d2fe"; // purple
            case 30911: return "#a5f3fc"; // skyblue
            case 31012: return "#cbd5e1"; // black
            case 31210: return "#bae6fd"; // blue
            case 31273: return "#fecdd3"; // red
            case 33811: return "#fbcfe8"; // pink
            default: return "#bae6fd";
        }
    }
}

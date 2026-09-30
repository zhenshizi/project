package servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.DiaryBean;
import model.KakeiboBean;
import model.UserBean;
import dao.DiaryDAO;
import dao.KakeiboDAO;
import dao.UserDAO;

@WebServlet("/calendar-day-share")
@MultipartConfig
public class CalendarDayShareServlet extends HttpServlet {
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
        int targetUserId = currentUser.getUserId();
        if (targetUserIdStr != null && !targetUserIdStr.trim().isEmpty()) {
            try {
                targetUserId = Integer.parseInt(targetUserIdStr.trim());
            } catch (NumberFormatException e) {
                targetUserId = currentUser.getUserId();
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

        KakeiboDAO kakeiboDAO = new KakeiboDAO();
        List<KakeiboBean> dailyList = kakeiboDAO.findDailyShared(targetUserId, sqlDate);

        BigDecimal dayTotal = BigDecimal.ZERO;
        for (KakeiboBean k : dailyList) {
            if (!"0".equals(k.getCategoryType())) {
                dayTotal = dayTotal.add(k.getYen());
            }
        }

        DiaryDAO diaryDAO = new DiaryDAO();
        DiaryBean diary = diaryDAO.findByDateAndUser(targetUserId, sqlDate, true);

        request.setAttribute("dateVal", dateVal);
        request.setAttribute("dateStr", dateVal.toString());
        request.setAttribute("prevDate", dateVal.minusDays(1).toString());
        request.setAttribute("nextDate", dateVal.plusDays(1).toString());
        request.setAttribute("dailyList", dailyList);
        request.setAttribute("dayTotal", dayTotal);
        request.setAttribute("diary", diary);

        request.setAttribute("groupMembers", groupMembers);
        request.setAttribute("targetUserId", targetUserId);
        request.setAttribute("isOwner", (targetUserId == currentUser.getUserId()));

        request.getRequestDispatcher("/WEB-INF/jsp/calendar-day-share.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        String action = request.getParameter("action");
        String dateStr = request.getParameter("date");
        String targetUserIdStr = request.getParameter("target_user_id");
        Date sqlDate = Date.valueOf(dateStr);

        if ("delete_kakeibo".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            KakeiboDAO dao = new KakeiboDAO();
            dao.deleteKakeibo(id, currentUser.getUserId());

            int sharedCount = dao.countSharedData(currentUser.getUserId());
            currentUser.setShare(sharedCount > 0);
            session.setAttribute("currentUser", currentUser);
        } else if ("save_diary".equals(action)) {
            String content = request.getParameter("content");
            Part filePart = request.getPart("photo");

            byte[] photoBytes = null;
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream is = filePart.getInputStream();
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) != -1) {
                        baos.write(buf, 0, len);
                    }
                    photoBytes = baos.toByteArray();
                }
            } else {
                DiaryDAO dDao = new DiaryDAO();
                DiaryBean existing = dDao.findByDateAndUser(currentUser.getUserId(), sqlDate, true);
                if (existing != null) {
                    photoBytes = existing.getPhoto();
                }
            }

            DiaryBean diary = new DiaryBean();
            diary.setUserId(currentUser.getUserId());
            diary.setDate(sqlDate);
            diary.setContent(content);
            diary.setPhoto(photoBytes);
            diary.setMode(true);

            DiaryDAO dDao = new DiaryDAO();
            dDao.saveOrUpdate(diary);
        }

        response.sendRedirect(request.getContextPath() + "/calendar-day-share?date=" + dateStr + "&target_user_id=" + (targetUserIdStr != null ? targetUserIdStr : currentUser.getUserId()));
    }
}

package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.DiaryDAO;
import model.DiaryBean;

@WebServlet("/diary-image")
public class DiaryImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdStr = request.getParameter("user_id");
        String dateStr = request.getParameter("date");
        String modeStr = request.getParameter("mode");
        boolean mode = "true".equalsIgnoreCase(modeStr);

        if (userIdStr != null && dateStr != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                java.sql.Date sqlDate = java.sql.Date.valueOf(dateStr);

                DiaryDAO dao = new DiaryDAO();
                DiaryBean diary = dao.findByDateAndUser(userId, sqlDate, mode);

                if (diary != null && diary.getPhoto() != null && diary.getPhoto().length > 0) {
                    response.setContentType("image/png");
                    response.getOutputStream().write(diary.getPhoto());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}

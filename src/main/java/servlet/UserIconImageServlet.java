package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.UserDAO;
import model.UserBean;

@WebServlet("/user-icon-image")
public class UserIconImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdStr = request.getParameter("user_id");

        if (userIdStr != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                UserDAO dao = new UserDAO();
                UserBean user = dao.findByUserId(userId);

                if (user != null && user.getUserIcon() != null && user.getUserIcon().length > 0) {
                    response.setContentType("image/png");
                    response.getOutputStream().write(user.getUserIcon());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}

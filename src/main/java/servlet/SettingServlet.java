package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserBean;
import dao.UserDAO;
import dao.GroupIdDAO;

@WebServlet("/setting")
public class SettingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        UserDAO userDAO = new UserDAO();
        UserBean dbUser = userDAO.findByUserId(currentUser.getUserId());

        request.setAttribute("user", dbUser);
        request.getRequestDispatcher("/WEB-INF/jsp/setting.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        String action = request.getParameter("action");
        UserDAO userDAO = new UserDAO();
        GroupIdDAO groupDAO = new GroupIdDAO();

        if ("logout".equals(action)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        } else if ("create_group".equals(action)) {
            String groupId = request.getParameter("group_id");
            if (groupId == null || groupId.trim().length() < 8) {
                request.setAttribute("err_create_group", "8ケタで入力してください");
            } else if (groupDAO.findByGroupId(groupId.trim())) {
                request.setAttribute("err_create_group", "他のIDと重複しています");
            } else {
                groupDAO.insertGroupId(groupId.trim(), currentUser.getUserId());
                userDAO.updateGroupId(currentUser.getUserId(), groupId.trim());
                currentUser.setGroupId(groupId.trim());
                session.setAttribute("currentUser", currentUser);

                request.setAttribute("showGroupPopup", true);
                request.setAttribute("popupGroupId", groupId.trim());
                request.setAttribute("popupEmail", currentUser.getMailAddress());
            }
        } else if ("auth_group".equals(action)) {
            String groupId = request.getParameter("auth_group_id");
            String creatorEmail = request.getParameter("creator_email");

            boolean hasErr = false;
            if (groupId == null || groupId.trim().isEmpty()) {
                request.setAttribute("err_auth_group", "共有IDを入力してください");
                hasErr = true;
            } else if (groupId.trim().length() < 8) {
                request.setAttribute("err_auth_group", "8ケタの数字を入力してください");
                hasErr = true;
            }

            if (creatorEmail == null || creatorEmail.trim().isEmpty()) {
                request.setAttribute("err_auth_email", "メールアドレスを入力してください");
                hasErr = true;
            }

            if (!hasErr) {
                boolean valid = groupDAO.authenticateGroup(groupId.trim(), creatorEmail.trim());
                if (!valid) {
                    request.setAttribute("err_auth_group", "共有IDまたは作成者のメールアドレスが正しくありません");
                } else {
                    userDAO.updateGroupId(currentUser.getUserId(), groupId.trim());
                    currentUser.setGroupId(groupId.trim());
                    session.setAttribute("currentUser", currentUser);

                    request.setAttribute("showAuthPopup", true);
                }
            }
        }

        UserBean dbUser = userDAO.findByUserId(currentUser.getUserId());
        request.setAttribute("user", dbUser);
        request.getRequestDispatcher("/WEB-INF/jsp/setting.jsp").forward(request, response);
    }
}

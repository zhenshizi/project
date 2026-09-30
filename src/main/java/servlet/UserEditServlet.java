package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserBean;
import dao.UserDAO;

@WebServlet("/user-edit")
@MultipartConfig
public class UserEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");
        
        UserDAO uDao = new UserDAO();
        UserBean user = uDao.findByUserId(currentUser.getUserId());
        request.setAttribute("user", user);

        request.getRequestDispatcher("/WEB-INF/jsp/user-edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        String iconPath = request.getParameter("selected_icon_path");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String secretQuestion = request.getParameter("secret_question");
        String secretAnswer = request.getParameter("secret_answer");
        String nickname = request.getParameter("nickname");

        boolean hasError = false;

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("err_name", "入力してください");
            hasError = true;
        }
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("err_email", "入力してください");
            hasError = true;
        } else if (!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            request.setAttribute("err_email", "使用できないメールアドレスです");
            hasError = true;
        } else {
            UserDAO uDao = new UserDAO();
            UserBean existing = uDao.findByMailAddress(email.trim());
            if (existing != null && existing.getUserId() != currentUser.getUserId()) {
                request.setAttribute("err_email", "使用できないメールアドレスです");
                hasError = true;
            }
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("err_password", "入力してください");
            hasError = true;
        } else if (password.length() < 8) {
            request.setAttribute("err_password", "8桁で入力してください");
            hasError = true;
        }

        if (secretQuestion == null || secretQuestion.trim().isEmpty()) {
            request.setAttribute("err_secretQuestion", "選択してください");
            hasError = true;
        }
        if (secretAnswer == null || secretAnswer.trim().isEmpty()) {
            request.setAttribute("err_secretAnswer", "入力してください");
            hasError = true;
        }
        if (nickname == null || nickname.trim().isEmpty()) {
            request.setAttribute("err_nickname", "入力してください");
            hasError = true;
        }

        UserDAO uDao = new UserDAO();
        UserBean user = uDao.findByUserId(currentUser.getUserId());

        if (hasError) {
            request.setAttribute("user", user);
            request.setAttribute("input_iconPath", iconPath);
            request.setAttribute("input_name", name);
            request.setAttribute("input_email", email);
            request.setAttribute("input_password", password);
            request.setAttribute("input_secretQuestion", secretQuestion);
            request.setAttribute("input_secretAnswer", secretAnswer);
            request.setAttribute("input_nickname", nickname);
            request.getRequestDispatcher("/WEB-INF/jsp/user-edit.jsp").forward(request, response);
            return;
        }

        if (iconPath != null && !iconPath.trim().isEmpty()) {
            String realPath = getServletContext().getRealPath("/" + iconPath);
            if (realPath != null) {
                File f = new File(realPath);
                if (f.exists()) {
                    try (FileInputStream fis = new FileInputStream(f)) {
                        user.setUserIcon(fis.readAllBytes());
                    }
                }
            }
        }

        user.setUserName(name);
        user.setMailAddress(email);
        user.setPassword(org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt(10)));
        user.setSecretQuestion(secretQuestion);
        user.setSecretAnswer(org.mindrot.jbcrypt.BCrypt.hashpw(secretAnswer, org.mindrot.jbcrypt.BCrypt.gensalt(10)));
        user.setNickname(nickname);

        uDao.updateUser(user);

        // 更新されたユーザー情報をセッションに反映
        session.setAttribute("currentUser", user);

        response.sendRedirect(request.getContextPath() + "/setting?showPopup=true");
    }
}

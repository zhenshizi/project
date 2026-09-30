package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "メールアドレスまたはパスワードを入力してください");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        dao.UserDAO userDAO = new dao.UserDAO();
        model.UserBean user = userDAO.findByMailAddress(email.trim());

        if (user == null || !org.mindrot.jbcrypt.BCrypt.checkpw(password, user.getPassword())) {
            request.setAttribute("errorMessage", "メールアドレスまたはパスワードが違います");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        // ログイン成功: セッション固定化攻撃対策（旧セッション破棄・新セッション作成）
        jakarta.servlet.http.HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        jakarta.servlet.http.HttpSession newSession = request.getSession(true);

        // 共有データのチェック
        dao.KakeiboDAO kakeiboDAO = new dao.KakeiboDAO();
        int sharedCount = kakeiboDAO.countSharedData(user.getUserId());
        user.setShare(sharedCount > 0);

        newSession.setAttribute("currentUser", user);

        response.sendRedirect(request.getContextPath() + "/calendar-monthly-private");
    }
}

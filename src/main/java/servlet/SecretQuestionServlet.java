package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/secret-question")
public class SecretQuestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/secret_question.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String secretQuestion = request.getParameter("secret_question");
        String secretAnswer = request.getParameter("secret_answer");

        if (email == null || email.trim().isEmpty() ||
            secretQuestion == null || secretQuestion.trim().isEmpty() ||
            secretAnswer == null || secretAnswer.trim().isEmpty()) {
            request.setAttribute("errorMessage", "すべての項目を入力してください");
            request.setAttribute("email", email);
            request.setAttribute("secretQuestion", secretQuestion);
            request.getRequestDispatcher("/WEB-INF/jsp/secret_question.jsp").forward(request, response);
            return;
        }

        dao.UserDAO userDAO = new dao.UserDAO();
        model.UserBean user = userDAO.findByMailAddress(email.trim());

        if (user == null || !secretQuestion.equals(user.getSecretQuestion()) ||
            !org.mindrot.jbcrypt.BCrypt.checkpw(secretAnswer, user.getSecretAnswer())) {
            request.setAttribute("errorMessage", "メールアドレスまたは秘密の質問・こたえが正しくありません");
            request.setAttribute("email", email);
            request.setAttribute("secretQuestion", secretQuestion);
            request.getRequestDispatcher("/WEB-INF/jsp/secret_question.jsp").forward(request, response);
            return;
        }

        // 照合成功：ログイン成功処理
        jakarta.servlet.http.HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        jakarta.servlet.http.HttpSession newSession = request.getSession(true);

        dao.KakeiboDAO kakeiboDAO = new dao.KakeiboDAO();
        int sharedCount = kakeiboDAO.countSharedData(user.getUserId());
        user.setShare(sharedCount > 0);

        newSession.setAttribute("currentUser", user);

        response.sendRedirect(request.getContextPath() + "/calendar-monthly-private");
    }
}

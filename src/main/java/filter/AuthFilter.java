package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserBean;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();

        // 認証除外パス
        if (path.endsWith("/login") || path.endsWith("/login.jsp") ||
            path.endsWith("/user-register") || path.endsWith("/user-register.jsp") ||
            path.endsWith("/secret-question") || path.endsWith("/secret-question.jsp") ||
            path.startsWith("/css/") || path.startsWith("/js/") ||
            path.startsWith("/img/") || path.startsWith("/images/") ||
            path.startsWith("/components/") || path.startsWith("/user-icon-image") || path.startsWith("/diary-image")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        UserBean currentUser = null;
        if (session != null) {
            currentUser = (UserBean) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}

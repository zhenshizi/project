package servlet;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.TodoBean;
import model.UserBean;
import dao.TodoDAO;

@WebServlet("/todo")
public class TodoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        TodoDAO dao = new TodoDAO();
        List<TodoBean> todoList = dao.findByUserId(currentUser.getUserId());

        String ideaText = "";
        for (TodoBean t : todoList) {
            if (t.getIdea() != null && !t.getIdea().trim().isEmpty()) {
                ideaText = t.getIdea();
                break;
            }
        }

        request.setAttribute("todoList", todoList);
        request.setAttribute("ideaText", ideaText);

        request.getRequestDispatcher("/WEB-INF/jsp/todo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");

        String action = request.getParameter("action");
        TodoDAO dao = new TodoDAO();

        if ("add_todo".equals(action)) {
            String text = request.getParameter("todo_text");
            if (text != null && !text.trim().isEmpty()) {
                TodoBean t = new TodoBean();
                t.setUserId(currentUser.getUserId());
                t.setTodo(text.trim());
                t.setStatus(false);
                dao.insertTodo(t);
            }
        } else if ("edit_todo".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String text = request.getParameter("todo_text");
            if (text != null && !text.trim().isEmpty()) {
                dao.updateTodoText(id, currentUser.getUserId(), text.trim());
            }
        } else if ("toggle_status".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            dao.updateTodoStatus(id, currentUser.getUserId(), status);
        } else if ("delete_todo".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteTodo(id, currentUser.getUserId());
        } else if ("save_idea".equals(action)) {
            String idea = request.getParameter("idea_text");
            dao.updateIdea(currentUser.getUserId(), idea != null ? idea.trim() : "");
        }

        response.sendRedirect(request.getContextPath() + "/todo");
    }
}

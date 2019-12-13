import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

public class Mypage extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException{

    request.setCharacterEncoding("utf-8");
    response.setContentType("text/html; charset=UTF-8");

    HttpSession session = request.getSession(true);

    String name = Objects.toString(request.getParameter("id"));
    String pass = Objects.toString(request.getParameter("password"));

    if(Objects.toString(request.getParameter("session")).equals("clear")){
      session.removeAttribute("id");
      session.removeAttribute("password");
    }

    // JSP に forward
    String view = "mypage.jsp";
    RequestDispatcher dispatcher = request.getRequestDispatcher(view);
    dispatcher.forward(request, response);
  }
}
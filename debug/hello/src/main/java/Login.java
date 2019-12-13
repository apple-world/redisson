import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Objects;

//import static com.sun.webpane.platform.ConfigManager.log;

public class Login extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException{

    request.setCharacterEncoding("utf-8");
    response.setContentType("text/html; charset=UTF-8");

    HttpSession session = request.getSession(true);
    String name = request.getParameter("id").toString();
    String pass = request.getParameter("password").toString();
    session.setAttribute("id", name);
    session.setAttribute("password", pass);

    log("---------------------------------------");
    log(Objects.toString(session.getId()));


    // JSP に forward
    String view = "login.jsp";
    RequestDispatcher dispatcher = request.getRequestDispatcher(view);
    dispatcher.forward(request, response);
  }
}
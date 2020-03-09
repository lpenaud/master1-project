package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helpers.HttpStatusCode;
import helpers.Servlet;
import session.Session;

/**
 * Servlet implementation class connection
 */
@WebServlet("/connection")
public class connection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public connection() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpStatusCode.NotFound.sendStatus(response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		if (Servlet.connect(login, password)) {
			Session s = Session.init(request);
			s.setLogin(login);
			s.setIdentifie(true);
			HttpStatusCode.Ok.sendStatus(response);
		} else {
			HttpStatusCode.Unauthorized.sendStatus(response);
		}
	}

}

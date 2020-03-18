package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import http.HttpStatusCode;
import http.Token;
import servlet.helpers.Servlet;
import servlet.helpers.Validator;

/**
 * Servlet implementation class connection
 */
@WebServlet("/connection")
public class Connection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Connection() {
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
		Validator validator = new Validator(request);
		String login = validator.getString("login");
		String password = validator.getString("password");
		if (validator.sendError(response)) {
			return;
		}
		if (Servlet.connect(login, password)) {
			Token token = new Token();
			token.send(response);
			return;
		}
		HttpStatusCode.Unauthorized.sendStatus(response);
	}

}

package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import base.Base;
import helpers.HttpStatusCode;
import models.Administrator;
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
		Base b = new Base();
		Boolean isConnected;
		Administrator admin = new Administrator(login, password);
		try {
			isConnected = admin.select(b);
		} catch (SQLException e) {
			isConnected = false;
			e.printStackTrace();
		}
		if (isConnected) {
			Session s = Session.init(request);
			s.setLogin(login);
			s.setIdentifie(isConnected);
			HttpStatusCode.Ok.sendStatus(response);
		} else {
			HttpStatusCode.Unauthorized.sendStatus(response);
		}
	}

}

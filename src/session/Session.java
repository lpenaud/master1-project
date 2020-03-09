package session;

import javax.servlet.http.HttpServletRequest;

public class Session {
	protected boolean connected;
	protected String login;
	
	public static Session init(HttpServletRequest request) {
		Session s = (Session) request.getSession().getAttribute("session");
		if (s == null) {
			s = new Session();
			request.getSession().setAttribute("session", s);
		}
		return s;
	}

	public boolean isIdentifie() {
		return connected;
	}

	public void setIdentifie(boolean connected) {
		this.connected = connected;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}

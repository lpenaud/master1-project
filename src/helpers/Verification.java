package helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Verification {
	protected List<String> errors;
	protected HttpServletRequest request;

	public Verification(HttpServletRequest request) {
		this.errors = new ArrayList<>();
		this.request = request;
	}

	public String checkField(String parameter, String regExp) {
		String value = this.request.getParameter(parameter);
		if (value == null) {
			this.errors.add(parameter);
			return null;
		}
		if (Pattern.matches(regExp, value)) {
			return value;
		}
		this.errors.add(parameter);
		return null;
	}

	public String getString(String parameter, Integer minLen) {
		String value = this.request.getParameter(parameter);
		if (value == null) {
			this.errors.add(parameter);
			return null;
		}
		if (value.length() >= minLen) {
			return value;
		}
		this.errors.add(parameter);
		return null;
	}

	public Integer getInteger(String parameter) {
		String value = this.checkField(parameter, "[0-9]+");
		if (value != null) {
			return Integer.parseInt(value);
		}
		return null;
	}
	
	public Long getLong(String parameter) {
		String value = this.checkField(parameter, "[0-9]+");
		if (value != null) {
			return Long.parseLong(value);
		}
		return null;
	}
	
	/**
	 * Send error to client if any
	 * @param response
	 * @return true if there are errors false otherwise
	 * @throws IOException 
	 */
	public boolean sendError(HttpServletResponse response) throws IOException {
		if (this.errors.size() == 0) {
			return false;
		}
		HttpStatusCode.UnprocessableEntity.send(response, ListHelpers.join(this.errors));
		return true;
	}
}

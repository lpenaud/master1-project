package helpers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import http.error.ErrorFields;

public class Validator {
	protected Set<String> errors;
	protected HttpServletRequest request;

	public Validator(HttpServletRequest request) {
		this.errors = new HashSet<>();
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
	
	public String getString(String parameter) {
		return this.getString(parameter, 1);
	}
	
	public String getNullableString(String parameter) {
		String value = this.request.getParameter(parameter);
		if (value == null || value.isEmpty()) {
			return null;
		}
		return value;
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
		String value = this.checkField(parameter, "/^\\s+$/");
		if (value != null) {
			return Long.parseLong(value);
		}
		return null;
	}
	
	public Long getNullableLong(String parameter) {
		String value = this.request.getParameter(parameter);
		if (value == null || value.isEmpty() || Pattern.matches("/^(\\s*|\\d+)$/", value)) {
			return null;
		}
		return Long.parseLong(value);
	}

	public Boolean getBoolean(String parameter) {
		String value = this.request.getParameter(parameter);
		if (value == null || value.equals("false")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Send error to client if any
	 * @param response
	 * @return true if there are errors false otherwise
	 * @throws IOException 
	 */
	public boolean sendError(HttpServletResponse response) throws IOException {
		if (this.errors.isEmpty()) {
			return false;
		}
		ErrorFields ef = new ErrorFields(this.errors);
		ef.sendError(response);
		return true;
	}
}

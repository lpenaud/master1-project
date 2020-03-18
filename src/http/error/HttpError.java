package http.error;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import http.HttpStatusCode;
import servlet.helpers.Servlet;

@JsonAutoDetect(fieldVisibility = Visibility.PROTECTED_AND_PUBLIC)
public abstract class HttpError {
	protected Integer code;
	protected String message;
	
	public HttpError(HttpStatusCode status) {
		this.code = status.getCode();
		this.message = status.getMessage();
	}
	
	public void sendError(HttpServletResponse response) throws IOException {
		response.setStatus(this.code);
		Servlet.sendJson(this, response);
	}
}

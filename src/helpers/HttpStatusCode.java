/**
 * 
 */
package helpers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public enum HttpStatusCode {
	
	Ok(200, "OK"),
	Created(201, "Created"),
	Unauthorized(401, "Unauthorized"),
	NotFound(404, "Not Found");
	
	private String message;
	private Integer code;
	
	private HttpStatusCode(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public Integer getCode() {
		return this.code;
	}

	public void sendStatus(HttpServletResponse response) throws IOException {
		response.setStatus(this.code);
		response.setContentType("text/plain");
		response.getWriter().append(this.message);
	}
}

package http.error;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import http.HttpStatusCode;

@JsonAutoDetect(fieldVisibility = Visibility.PROTECTED_AND_PUBLIC)
public class ImageNotFound extends HttpError {
	protected String name;

	public ImageNotFound(String name) {
		super(HttpStatusCode.NotFound);
		this.name = name;
	}

}

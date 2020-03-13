package http.error;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import http.HttpStatusCode;

@JsonAutoDetect(fieldVisibility = Visibility.PROTECTED_AND_PUBLIC)
public class ErrorFields extends HttpError {
	protected List<String> missing;

	public ErrorFields(List<String> missing) {
		super(HttpStatusCode.UnprocessableEntity);
		this.missing = missing;
	}

}

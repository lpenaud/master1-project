package http.error;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import http.HttpStatusCode;

@JsonAutoDetect(fieldVisibility = Visibility.PROTECTED_AND_PUBLIC)
public class ErrorFields extends HttpError {
	protected Collection<String> missing;

	public ErrorFields(Collection<String> missing) {
		super(HttpStatusCode.UnprocessableEntity);
		this.missing = missing;
	}

}

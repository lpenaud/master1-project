package http;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import helpers.Config;
import helpers.Crypt;

public class Token {
	public static final String headerName = "Token";
	
	public Long timestamp;
	public Long ttl;
	public String sign;
	
//	public static boolean isConnected(HttpServletRequest request) {
//		String token64 = request.getHeader(headerName);
//		if (token64 != null && token64.) {
//			
//		}
//	}
	
	public static Token from(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, Token.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Token from(HttpServletRequest request) {
		return from(request.getHeader(headerName));
	}
	
	public Token() throws NoSuchAlgorithmException {
		this(System.currentTimeMillis());
	}
	
	public Token(Long timestamp) throws NoSuchAlgorithmException {
		this.timestamp = timestamp;
		this.ttl = Config.config.getTtl();
		this.sign = Crypt.getSHA(Config.config.getSecret());
	}
	
	public byte[] toJson() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsBytes(this);
	}
	
	public void send(HttpServletResponse response) throws JsonProcessingException, IOException {
		response.setContentType("text/plain");
		response.getOutputStream().write(Base64.getEncoder().encode(this.toJson()));
	}

}

package http;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.Config;
import helpers.Crypt;

public class Token {
	public static final String headerName = "Token";
	
	public Long timestamp;
	public Long ttl;
	public String sign;
	
	public static boolean isConnected(HttpServletRequest request) {
		String token64 = request.getHeader(headerName);
		if (token64 == null || token64.isEmpty()) {
			return false;
		}
		return from(Base64.getDecoder().decode(token64)).isValid();
	}

	public static Token from(byte[] json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Token token = mapper.readValue(json, Token.class);
			return token;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

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

	public Token() {
		this(System.currentTimeMillis());
	}

	public Token(Long timestamp) {
		this.timestamp = timestamp;
		this.ttl = Config.config.getTtl();
		try {
			this.sign = getSHA();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] toJson() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsBytes(this);
	}
	
	public void send(HttpServletResponse response) throws JsonProcessingException, IOException {
		response.setContentType("application/plain");
		response.getOutputStream().write(Base64.getEncoder().encode(this.toJson()));
	}
	
	@JsonIgnore
	public boolean isValid() {
		try {
			if (!this.sign.equals(getSHA())) {
				System.out.println("invalid sign");
				System.out.println(String.format("Current: %s, SHA: %s", this.sign, getSHA()));
				return false;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		if (this.timestamp + this.ttl < System.currentTimeMillis()) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("{");
		builder.append("timestamp: ");
		builder.append(this.timestamp);
		builder.append(", ttl: ");
		builder.append(this.ttl);
		builder.append(", sign: ");
		builder.append(this.sign);
		builder.append("}");
		return builder.toString();
	}
	
	private static String getSHA() throws NoSuchAlgorithmException {
		return Crypt.getSHA(Config.config.getSecret());
	}

}

package servlet.helpers;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import config.Config;
import http.error.ErrorFields;

public class PartFormater {
	protected HttpServletRequest request;
	protected Set<String> errors;
	
	public class File {
		private Path pathname;
		private Part part;
		
		protected File(Part part, Path pathname) {
			this.pathname = pathname;
			this.part = part;
		}

		public String getContentType() {
			return this.part.getContentType();
		}

		public Path getPathname() {
			return pathname;
		}
		
		public void write() throws IOException {
			this.part.write(this.pathname.toString());
		}
	}
	
	public PartFormater(HttpServletRequest request) {
		this.request = request;
		this.errors = new HashSet<>();
	}
	
	public String readString(String name) {
		return this.readString(name, 1);
	}
	
	public String readString(String name, int minLen) {
		return this.readString(name, minLen, 255);
	}
	
	public String readString(String name, int minLen, int len) {
		String res;
		Part part;
		byte[] b;
		try {
			b = new byte[len];
			part = this.request.getPart(name);
			part.getInputStream().read(b);
		} catch (Exception e) {
			e.printStackTrace();
			this.errors.add(name);
			return null;
		}
		res = new String(b).trim();
		if (res.length() >= minLen) {
			return res;
		}
		this.errors.add(name);
		return null;
	}
	
	public Long readLong(String name) {
		try {
			return Long.valueOf(this.readString(name));
		} catch (NumberFormatException e) {}
		this.errors.add(name);
		return null;
	}
	
	public Integer readInteger(String name) {
		try {
			return Integer.valueOf(this.readString(name));
		} catch (NumberFormatException e) {}
		this.errors.add(name);
		return null;
	}
	
	public File getFile(String name) throws IOException {
		Part part;
		try {
			part = this.request.getPart(name);
		} catch (Exception e) {
			this.errors.add(name);
			e.printStackTrace();
			return null;
		}
		String dst = Servlet.randomFilename() + "." + part.getContentType().split("/")[1];
		Path abs = Paths.get(Config.config.getBucket(), dst);
		return new File(part, abs);
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

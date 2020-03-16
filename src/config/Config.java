package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Config {
	public static Config config = new Config("config/config");
	
	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getBucket() {
		return bucket;
	}
	
	public String getSecret() {
		return this.secret;
	}
	
	public Long getTtl() {
		return this.ttl;
	}
	
	protected String url;
	protected String user;
	protected String password;
	protected String bucket;
	protected String secret;
	protected Long ttl;
	
	private Config(String pathname) {
		ResourceBundle rb = ResourceBundle.getBundle(pathname);
		this.url = rb.getString("url");
		this.user = rb.getString("user");
		this.password = rb.getString("password");
		try {
			this.bucket = this.getBucketLocation(rb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			this.ttl = Long.valueOf(rb.getString("ttl"));
		} catch (MissingResourceException e) {
			this.ttl = 3600000L;
		}
		try {
			this.secret = rb.getString("secret");
		} catch (MissingResourceException e) {
			this.secret = "secret";
		}
	}
	
	private String getBucketLocation(ResourceBundle rb) throws FileNotFoundException {
		File f = Paths.get(rb.getString("bucket")).toFile();
		if (!(f.exists() || f.isDirectory())) {
			throw new FileNotFoundException("bucket must exist and be a directory");
		}
		return f.toString();
	}
}

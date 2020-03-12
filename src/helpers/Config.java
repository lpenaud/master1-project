package helpers;

import java.util.ResourceBundle;

public class Config {
	public static Config config = new Config("config/config");
	
	protected String url;
	protected String user;
	protected String password;
	protected String bucket;
	
	public Config(String pathname) {
		ResourceBundle rb = ResourceBundle.getBundle(pathname);
		this.url = rb.getString("url");
		this.user = rb.getString("user");
		this.password = rb.getString("password");
		this.bucket = rb.getString("bucket");
	}
	
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
}

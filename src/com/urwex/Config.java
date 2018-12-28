package com.urwex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private Properties properties = null;
	public String geckoDriverPath;	
	public String connectionString;	
	public String username;
	public String password;
	public long pollInterval;
	
	public Config()
	{		
		try {
			File file = new File("config.xml");
			FileInputStream fileInputStream = new FileInputStream(file);
			this.properties = new Properties();
			this.properties.loadFromXML(fileInputStream);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key)
	{
		String result = this.properties.getProperty(key);
		return result == null? "": result;
	}
	
	public String getConnectionString() {
		return this.properties.getProperty("connectionString");
	}
	
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	
	public String getUsername() {
		return this.properties.getProperty("username");
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.properties.getProperty("password");
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public long getPollInterval() {
		return Long.valueOf(this.properties.getProperty("pollInterval", "0"));
	}
	
	public void setPollInterval(long pollInterval) {
		this.pollInterval = pollInterval;
	}
	
	public String getGeckoDriverPath() {
		return this.properties.getProperty("geckodriver");
	}

	public void setGeckoDriverPath(String geckoDriverPath) {
		this.geckoDriverPath = geckoDriverPath;
	}
}

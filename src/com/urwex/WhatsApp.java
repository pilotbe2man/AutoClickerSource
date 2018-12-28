package com.urwex;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import javax.net.ssl.HttpsURLConnection;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class WhatsApp implements Runnable {
//	public final String CONTAINER_CLASS_NAME = "pluggable-input-body";
	public final String CONTAINER_CLASS_NAME = "/html[1]/body[1]/div[1]/div[1]/div[1]/div[4]/div[1]/footer[1]/div[1]/div[2]/div[1]/div[2]";
	public boolean continueRunning = true;

	public void run() {
		// Initialize
		Scanner sc = null;
		WebDriver driver = null;		
		try
		{
			//set firefox binary bin
//			File pathToBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
//			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_BINARY, pathToBinary.getAbsolutePath());

			// Get config
			Config config = new Config();
			System.setProperty("webdriver.gecko.driver", config.getGeckoDriverPath());

			FirefoxOptions options = new FirefoxOptions();
			options.setLogLevel(Level.ALL);

			// Init driver
			driver = new FirefoxDriver(options);
			driver.get("https://web.whatsapp.com");
			
			// Wait for the user to sign in
			Thread.sleep(10000);
			
			// Process
			sc = new Scanner(System.in);
			System.out.print("Enter the chat thread number: ");
			int ans = sc.nextInt();

			System.out.print("clicked thread item : " + ans);
			driver.findElement(By.xpath("//*[@id=\"pane-side\"]/div/div/div/div["+ans+"]/div/div/div[2]/div[1]/div[1]/span")).click();
			System.out.print("clicked input field----");
//			driver.findElement(By.className(CONTAINER_CLASS_NAME)).click();
			driver.findElement(By.xpath(CONTAINER_CLASS_NAME)).click();
			System.out.print("receiving list from server ----");
//			String username = driver.findElement(By.xpath("//*[@id=\"main\"]/header/div[2]/div/div/span")).getText();
			
			while(continueRunning)
			{	
				// Wait for post interval period
				Thread.sleep(config.getPollInterval() * 1000);
				// Get data to post
				ArrayList<String> data = getListings();
				if(data.size()>0)
				{
//					driver.findElement(By.className(CONTAINER_CLASS_NAME)).sendKeys(config.getProperty("Heading"));
					driver.findElement(By.xpath(CONTAINER_CLASS_NAME)).sendKeys(config.getProperty("Heading"));
				}
				// Post
				for(int i=0;i<data.size();i++)
				{
					if(i%2==0)
					{
//						driver.findElement(By.className(CONTAINER_CLASS_NAME)).sendKeys(Keys.SHIFT, Keys.ENTER);
						driver.findElement(By.xpath(CONTAINER_CLASS_NAME)).sendKeys(Keys.SHIFT, Keys.ENTER);
					}
					else
					{
//						driver.findElement(By.className(CONTAINER_CLASS_NAME)).sendKeys(" - ");
						driver.findElement(By.xpath(CONTAINER_CLASS_NAME)).sendKeys(" - ");
					}
//					driver.findElement(By.className(CONTAINER_CLASS_NAME)).sendKeys(data.get(i));
					driver.findElement(By.xpath(CONTAINER_CLASS_NAME)).sendKeys(data.get(i));
				}
//				driver.findElement(By.className(CONTAINER_CLASS_NAME)).sendKeys(Keys.ENTER);
				driver.findElement(By.xpath(CONTAINER_CLASS_NAME)).sendKeys(Keys.ENTER);
				//driver.findElement(By.className("icon-send")).click();
			}
		}
		catch(InterruptedException ie)
		{
			System.err.println("The process is interrupted");
		}
		catch(Exception e)
		{
			System.err.println("A generic error occurred");
			e.printStackTrace();
		}
		finally
		{
			sc.close();
			driver.quit();
		}
	}
	
	public ArrayList<String> getListings() throws IOException {
		ArrayList<String> resultStr = new ArrayList<String>();
		Config config = new Config();
		String protocol = config.getProperty("protocol");
		String hostname = config.getProperty("hostname");
		URL obj = new URL(protocol + "://" + hostname + "/Feed/");
		HttpURLConnection con = null;
		if(protocol.equalsIgnoreCase("http"))
		{
			con = (HttpURLConnection) obj.openConnection();			
		}
		else
		{
			con = (HttpsURLConnection) obj.openConnection();
		}
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write("key=bD4GVlZWNBd".getBytes());
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();		
		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			try {
				JSONObject jsObj = new JSONObject(response.toString());
				String status = jsObj.getString("success"); 
				if(status.equalsIgnoreCase("true"))
				{
					JSONArray jsArr = (JSONArray) jsObj.get("listing");
					for(int i=0; i<jsArr.length(); i++)
					{
						JSONObject subJsObj = jsArr.getJSONObject(i);
						resultStr.add(subJsObj.getString("txt"));
						resultStr.add(subJsObj.getString("url"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Request unsuccessful");
		}
		return resultStr;
	}

}

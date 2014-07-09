import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class curlTrail {
	
	static JSONArray jsonArray;
	static JSONParser parser; 
	static int count=0;
	static HttpClient client;
	static String jobID="";
	static String status="";
	HttpResponse response;
	public static void main(String k[]) throws IOException, InterruptedException, ParseException 
	{
		curlTrail ct=new curlTrail();
		client = new DefaultHttpClient();
		
		//List of browsers
		System.out.println("List of browsers");
		ct.makeGetRequest("http://api.browserstack.com/3/browsers");
		
		//Getting the set of browsers to be tested
		parser = new JSONParser();
		Object obj = parser.parse(new FileReader("C:\\JAVA\\Selenium\\browsers.json"));//path where your JSON file is stored
		jsonArray = (JSONArray) obj;

		for(int i=0;i<jsonArray.size();i++,count++)
		{
			
			//Create a worker
			System.out.println("Creating a worker "+count);
			Boolean flag = ct.makePostRequest("http://api.browserstack.com/3/worker","http://www.gsmarena.com");//The second parameter is the link on which you wish to do JS Testing
			
			Thread.sleep(5000);
			
			//Get current worker status
			if(flag)
			{
				System.out.println("Getting current worker status");
				status=ct.makeGetRequest("http://api.browserstack.com/3/worker/"+jobID);
			}
			
			else
			{
				System.out.println("\tWorker not created");
				continue;
			}
			
			//Get total workers status
			System.out.println("Getting total workers status");
			ct.makeGetRequest("http://api.browserstack.com/3/workers");
			
			//Get API status
			System.out.println("Getting API status");
			ct.makeGetRequest("http://api.browserstack.com/3/status");
			
			Thread.sleep(10000);
			
			//Take Screenshot
			System.out.println("Taking Screenshot");
			ct.takeScreenshot("http://api.browserstack.com/3/worker/"+jobID+"/screenshot.png","text/xml");
			
			Thread.sleep(5000);
		
			//Delete current worker
			System.out.println("Deleting current worker");
			ct.makeDeleteRequest("http://api.browserstack.com/3/worker/"+jobID);
			
		}
	}
		
		
	public String makeGetRequest(String url) throws IOException
	{
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader("Accept","application/json");
		getRequest.setHeader("Content-type","application/json");

		String key = "<username>:<access-key>";
		String encoding = Base64.encodeBase64String(key.getBytes());
		getRequest.setHeader("Authorization", "Basic " + encoding);

		// Get the responses
		response = client.execute(getRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) 
		{
			System.out.println(line);
			System.out.println();
		}
		return line;
	}
	
	public Boolean makePostRequest(String url, String url2) throws IOException, ParseException
	{	
		HttpPost postRequest = new HttpPost(url);
		JSONObject obj=(JSONObject)jsonArray.get(count);

//		Content-type and Accept header not required

		String key = "<username>:<access-key>";
		String encoding = Base64.encodeBase64String(key.getBytes());
		postRequest.setHeader("Authorization", "Basic " + encoding);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("os", obj.get("os")+""));
		nameValuePairs.add(new BasicNameValuePair("os_version", obj.get("os_version")+""));
		nameValuePairs.add(new BasicNameValuePair("browser", obj.get("browser")+""));
		nameValuePairs.add(new BasicNameValuePair("browser_version", obj.get("browser_version")+""));
		nameValuePairs.add(new BasicNameValuePair("url", url2));
		nameValuePairs.add(new BasicNameValuePair("build", "JS Testing on Java"));
		postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Get the responses
		response = client.execute(postRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) 
		{
			System.out.println(line);
			JSONObject JSobj = (JSONObject) parser.parse(line);
			jobID=JSobj.get("id")+"";
			if(!line.contains("error"))
			{
				System.out.println("\tJobID="+jobID);
				System.out.println();
			}
			else
			{
				jobID=null;
				return false;
			}
		}
		return true;
	}
	
	
	public void makeDeleteRequest(String url) throws IOException, InterruptedException
	{
		EntityUtils.consume(response.getEntity());
		
		HttpDelete deleteRequest = new HttpDelete(url);
		
		deleteRequest.setHeader("Accept","application/json");
		deleteRequest.setHeader("Content-type","application/json");

		String key = "<username>:<access-key>";
		String encoding = Base64.encodeBase64String(key.getBytes());
		deleteRequest.setHeader("Authorization", "Basic " + encoding);

		// Get the responses
		response = client.execute(deleteRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) 
		{
			System.out.println(line);
			System.out.println();
		}
	}
	
	public void takeScreenshot(String url,String type)
	{
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader("Accept",type);
		getRequest.setHeader("Content-type",type);

		String key = "<username>:<access-key>";
		String encoding = Base64.encodeBase64String(key.getBytes());
		getRequest.setHeader("Authorization", "Basic " + encoding);

		// Get the responses
		try
		{
		response = client.execute(getRequest);
		System.out.println("Screenshot taken");
		System.out.println(response);
		}
		catch (Exception e)
		{
			System.out.println("Exception occured. Screenshot not taken");
		}
	}
}

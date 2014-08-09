import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Util 
{
	static HttpClient client = new DefaultHttpClient();;
	static HttpResponse response;
	static JSONParser parser; 
	
	public static HttpResponse makeGetRequest(String url,String key, String type) throws IOException, ParseException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader("Accept",type);
		getRequest.setHeader("Content-type",type);

		String encoding = Base64.encodeBase64String(key.getBytes());
		getRequest.setHeader("Authorization", "Basic " + encoding);

		// Get the responses
		response = client.execute(getRequest);
		return response;
	}
	
	public static HttpResponse makePostRequest(String url, String url2, String key, JSONObject obj) throws IOException, ParseException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		HttpPost postRequest = new HttpPost(url);

//		Content-type and Accept header not required
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
		return response;
	}
	
	public static HttpResponse makeDeleteRequest(String url, String key, String type) throws IOException, InterruptedException, ParseException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		HttpDelete deleteRequest = new HttpDelete(url);
		
		deleteRequest.setHeader("Accept",type);
		deleteRequest.setHeader("Content-type",type);

		String encoding = Base64.encodeBase64String(key.getBytes());
		deleteRequest.setHeader("Authorization", "Basic " + encoding);

		// Get the responses
		response = client.execute(deleteRequest);
		return response;
	}
	
	public static HttpResponse takeScreenshot(String url,String key,String type) throws IOException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader("Accept",type);
		getRequest.setHeader("Content-type",type);

		String encoding = Base64.encodeBase64String(key.getBytes());
		getRequest.setHeader("Authorization", "Basic " + encoding);

		// Get the responses
		try
		{
		response = client.execute(getRequest);
		System.out.println("Screenshot taken");
		return response;
		}
		catch (Exception e)
		{
			System.out.println("Exception occured. Screenshot not taken");
			System.out.println(e);
			return null;
		}
	}


}

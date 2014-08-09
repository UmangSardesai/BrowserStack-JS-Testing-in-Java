import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Client extends Util
{
	JSONArray jsonArray;
	final String BS_API= "http://api.browserstack.com/3/";
	String key = "";
	String type = "application/json";
	
	public Client(String username, String accesskey)
	{
		key=username+":"+accesskey;
	}
	
	public Client()
	{
		Scanner sc=new Scanner (System.in);
		System.out.println("Enter username");
		String us=sc.next();
		System.out.println("Enter accesskey");
		String ak=sc.next();
		key=us+":"+ak;
	}
	
	public JSONObject getListOfBrowsers() throws IOException, ParseException
	{
		HttpResponse response = Util.makeGetRequest(BS_API+"browsers",key,type);
		JSONObject jsonResponse=convertToJSON(response);
		return jsonResponse;
	}
	
	public JSONObject createWorker(JSONObject params, String url) throws IOException, ParseException
	{
		HttpResponse response = Util.makePostRequest(BS_API+"worker",url,key,params);
		JSONObject jsonResponse=convertToJSON(response);
		return jsonResponse;
	}
	
	public JSONObject getWorkerStatus(String jobID) throws IOException, ParseException
	{
		HttpResponse response = Util.makeGetRequest(BS_API+"worker/"+jobID,key,type);
		JSONObject jsonResponse=convertToJSON(response);
		return jsonResponse;
	}
	
	public JSONArray getTotalWorkersStatus() throws IOException, ParseException
	{
		HttpResponse response = Util.makeGetRequest(BS_API+"workers",key,type);
		JSONArray jsonResponse=convertToJSONArray(response);
		return jsonResponse;
	}
	
	public JSONObject getAPIStatus() throws IOException, ParseException
	{
		HttpResponse response = Util.makeGetRequest(BS_API+"status",key,type);
		JSONObject jsonResponse=convertToJSON(response);
		return jsonResponse;		
	}
	
	public void takeScreenshotAsIMG(String jobID, String path) throws IOException, ParseException
	{
		HttpResponse response = Util.takeScreenshot(BS_API+"worker/"+jobID+"/screenshot.png",key,"image/png");
		convertToIMG(response, path);
	}
	
	public String takeScreenshotAsXML(String jobID) throws IOException, ParseException
	{
		HttpResponse response = Util.takeScreenshot(BS_API+"worker/"+jobID+"/screenshot.xml",key,"text/xml");
		return convertToString(response);
	}
	
	public JSONObject takeScreenshotAsJSON(String jobID) throws IOException, ParseException
	{
		HttpResponse response = Util.takeScreenshot(BS_API+"worker/"+jobID+"/screenshot.json",key,"text/json");
		return convertToJSON(response);
	}
	
	public JSONObject deleteWorker(String jobID) throws IOException, InterruptedException, ParseException
	{
		HttpResponse response =Util.makeDeleteRequest(BS_API+"worker/"+jobID, key, type);
		JSONObject jsonResponse=convertToJSON(response);
		return jsonResponse;
	}
	
	
	public static JSONObject convertToJSON(HttpResponse response) throws ParseException, IOException
	{
		if (response!=null)
		{
			parser = new JSONParser();
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			line = rd.readLine();
			JSONObject JSobj = (JSONObject) parser.parse(line);
			return JSobj;
		}
		return null;
	}

	public static String convertToString(HttpResponse response) throws ParseException, IOException
	{
		if(response!=null)
		{
			parser = new JSONParser();
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			String line2 = "";
			while ((line = rd.readLine()) != null) 
			{
				line2+=line+"\n";
			}
			return line2;
		}
		return "";
	}
	
	public static JSONArray convertToJSONArray(HttpResponse response) throws ParseException, IOException
	{
		if (response!=null)
		{
			parser = new JSONParser();
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			line = rd.readLine();
			JSONArray JSarr = (JSONArray) parser.parse(line);
			return JSarr;
		}
		return null;
	}
	
	public static void convertToIMG(HttpResponse response,String path) throws IllegalStateException, IOException
	{
		if (response!=null)
		{
			FileOutputStream output = null;
			try
			{
				output = new FileOutputStream(path);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				System.out.println("Invalid path. Changing path to current directory");
				path=System.getProperty("user.dir")+"\\Screenie.bmp";
				output = new FileOutputStream(path);
			}
		    int bufferSize = 1024;
		    byte[] buffer = new byte[bufferSize];
		    int len = 0;
		    while ((len = response.getEntity().getContent().read(buffer)) != -1) {
		        output.write(buffer, 0, len);
		    }
		    output.close();
		    System.out.println("Screenshot saved as "+path);
		}
	}
}

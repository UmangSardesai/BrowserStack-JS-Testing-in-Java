import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.HttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;


public class Main extends Client
{
	static JSONArray jsonArray;
	static JSONParser parser;
	static HttpClient client;
	static String jobID="";
	static String imgString="";
	
	public static void main(String k[]) throws IOException, ParseException, InterruptedException, SAXException, ParserConfigurationException
	{
		Client cl=new Client("umangsardesai1","jSKjzgP8o2X41YZArqR1");
		
		System.out.println("List of Browsers:");
		System.out.println(cl.getListOfBrowsers()+"\n");
		
		try
		{
			parser = new JSONParser();
			Object obj = parser.parse(new FileReader("C:\\Selenium\\Java\\browsers.json"));//path where your JSON file is stored
			jsonArray = (JSONArray) obj;
		}
		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}
				
		for(int i=0;i<jsonArray.size();i++)
		{
			JSONObject j=(JSONObject)jsonArray.get(i);
			
			System.out.println("Creating Worker "+(i+1)+":");
			j=cl.createWorker(j,"http://google.com");//The URL on which you wish to test
			if((j+"").contains("error"))
			{
				System.out.println("Worker not created ");
				System.out.println("Error: "+j.get("errors")+"\n");
				continue;
			}						
			jobID=j.get("id")+"";
			System.out.println("JobID: "+jobID+"\n");
			
			System.out.println("Current Worker Status:");
			System.out.println(cl.getWorkerStatus(jobID)+"");
			while(cl.getWorkerStatus(jobID).get("status").equals("queue"))
			{
				System.out.println("Worker still in queue...");	
				Thread.sleep(3000);
			}
			
			System.out.println("\nTotal Worker Status: ");
			System.out.println(cl.getTotalWorkersStatus()+"\n");
			
			System.out.println("API Status: ");
			System.out.println(cl.getAPIStatus()+"\n");
			Thread.sleep(10000);
			
			System.out.println("Taking screenshot as XML");
			imgString=cl.takeScreenshotAsXML(jobID);
			System.out.println(imgString);
			Thread.sleep(3000);
			
			System.out.println("Taking screenshot as JSON");
			JSONObject jObj=cl.takeScreenshotAsJSON(jobID);
			System.out.println("Screenshot Link: "+jObj.get("url")+"\n");
			Thread.sleep(5000);
			
			System.out.println("Taking screenshot as IMG");
			cl.takeScreenshotAsIMG(jobID, "C:\\Selenium\\Java\\Screenshots\\Screenie.bmp");//path where the screenshot gets saved
			System.out.println();
			Thread.sleep(3000);
			
			System.out.println("Deleting Current Worker");
			System.out.println(cl.deleteWorker(jobID)+"\n");
		}
	}
}

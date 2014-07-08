BrowserStack-JS-Testing-in-Java
===============================

BrowserStack API for JavaScript Testing implemented using Java


###What is HTTPCLIENT ?
You retrieve and send data via the HttpClient class. An instance of this class can be created with new DefaultHttpClient();
DefaultHttpClient is the standard HttpClient and uses the SingleClientConnManager class to handle HTTP connections. 
SingleClientConnManager is not thread-safe, this means that access to it via several threads will create problems.
The HttpClient uses a HttpUriRequest to send and receive data. Important subclass of HttpUriRequest are HttpGet and HttpPost. 
You can get the response of the HttpClient as an InputStream.

###JAR files needed 
1. All the 7 JAR files from httpcomponents-client-4.3.4/lib 
2. json-simple-1.1.1.jar

###Code Explanation
All the JS testing is done on BrowserStack.
This httpclient has to make a request to http://api.browserstack.com/3 (here 3 is the version no.)
Headers such as (-H command in curl) is done using setHeader function.
Note: The headers are not used in Post request, though are used in cURL.

Authentication of username and password (-U command in curl) is done using same function, but you have to explicitly mention "Authentication" in the function parameters
Also the key has to be username and pw has to be encoded for which we need the common codec JAR.

The data parameters(-d command in curl) is sent using setEntity function. 
First you make a arraylist of all the parameters. 
The arraylist has to be of type of 'NameValuePair'.

We use HttpGet object for GET request, HttpPost object for POST request and so on.

the request is sent to BS using execute method.
the return is a HttpResponse which u can print.

**Note:** Before deleting a worker the request created for taking a screenshot must be closed.
this is done using EntityUtils.consume(response.getEntity());

###Running JS Testing for different browsers
Javascript testing can run only one test at a time.
So all the different parameters for a particular test case is stored in a JSON file (browsers.json).
All the different sets from this JSON file are extracted and stored in JSONArray.
Each set is a JSONObject.
You run the test in a loop taking one set at a time.

package fivetests;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
public class FiveTests 
{
	String key="9670d0f2f831b1d21ce3744b0ab6f6b6";
	String invalidapikey="Invalid API key. Please see http://openweathermap.org/faq#error401 for more info.";
	static String id1;
	static String id2;
	@Test
	public void test1() throws Exception
	{
		RestAssured.baseURI="http://api.openweathermap.org";
		RequestSpecification httprqst=RestAssured.given();
		JSONObject parameter=new JSONObject();

		JSONParser jsonparser=new JSONParser();
		FileReader reader=new FileReader(".\\jsonfiles\\data1.json");
		Object obj1=jsonparser.parse(reader);
		parameter=(JSONObject)obj1;
		
		httprqst.header("Content-Type","application/json");
		httprqst.body(parameter.toJSONString());
		Response response=httprqst.request(Method.POST,"/data/3.0/stations");
		
		JsonPath jo=new JsonPath(response.body().asString());	
		Assert.assertEquals(401, response.getStatusCode());
		Assert.assertEquals(invalidapikey, jo.get("message"));
	}
	
	@Test
	public void test2() throws Exception
	{
		RestAssured.baseURI="http://api.openweathermap.org";
		RequestSpecification httprqst=RestAssured.given();
		httprqst.queryParam("appid", key);
		httprqst.header("Content-Type","application/json");
		
		JSONObject parameter=new JSONObject();
		JSONParser jsonparser=new JSONParser();
		FileReader reader1=new FileReader(".\\jsonfiles\\data1.json");
		FileReader reader2=new FileReader(".\\jsonfiles\\data2.json");
		Object obj1=jsonparser.parse(reader1);
		Object obj2=jsonparser.parse(reader2);
		
		parameter=(JSONObject)obj1;
		httprqst.body(parameter.toJSONString());
		Response response=httprqst.request(Method.POST,"/data/3.0/stations");
		JsonPath jo1=new JsonPath(response.body().asString());	
		id1=jo1.get("ID");
		Assert.assertEquals(201,response.getStatusCode());
		
		parameter=(JSONObject)obj2;
		httprqst.body(parameter.toJSONString());
		response=httprqst.request(Method.POST,"/data/3.0/stations");
		JsonPath jo2=new JsonPath(response.body().asString());
		id2=jo2.get("ID");
		Assert.assertEquals(201,response.getStatusCode());
	}
	
	@Test
	public void test3() throws Exception
	{
		RestAssured.baseURI="http://api.openweathermap.org";
		RequestSpecification httprqst=RestAssured.given();
		httprqst.queryParam("appid", key);
		String i1=id1;

		Response response=httprqst.request(Method.GET,"/data/3.0/stations"+"/"+id1);
		JsonPath jo1=new JsonPath(response.body().asString());	
		Assert.assertEquals("DEMO_TEST001", jo1.get("external_id"));
		Assert.assertEquals("Markapur",jo1.get("name") );
		Assert.assertEquals(33.33f, jo1.get("latitude"));
		Assert.assertEquals(-111.43f,jo1.get("longitude"));
		Assert.assertEquals(444, jo1.get("altitude"));

		String i2=id2;

		response=httprqst.request(Method.GET,"/data/3.0/stations"+"/"+id2);
		JsonPath jo2=new JsonPath(response.body().asString());
		Assert.assertEquals("interview1", jo2.get("external_id"));
		Assert.assertEquals("Ongole",jo2.get("name") );
		Assert.assertEquals(33.44f, jo2.get("latitude"));
		Assert.assertEquals(-12.44f,jo2.get("longitude"));
		Assert.assertEquals(444, jo2.get("altitude"));
	}
	
	@Test
	public void test4() throws Exception
	{
		RestAssured.baseURI="http://api.openweathermap.org";
		RequestSpecification httprqst=RestAssured.given();
		httprqst.queryParam("appid", key);
		String ep="/"+id1;
		Response response=httprqst.request(Method.DELETE,"/data/3.0/stations"+ep);
		JsonPath jo1=new JsonPath(response.body().asString());	
		Assert.assertEquals(204,response.getStatusCode());
		ep="/"+id2;
		response=httprqst.request(Method.DELETE,"/data/3.0/stations"+ep);
		JsonPath jo2=new JsonPath(response.body().asString());
		Assert.assertEquals(204,response.getStatusCode());
		Thread.sleep(2000);
	}
	
	@Test
	public void test5() throws Exception
	{
		RestAssured.baseURI="http://api.openweathermap.org";
		RequestSpecification httprqst=RestAssured.given();
		httprqst.queryParam("appid", key);
		
		String ep="/"+id1;
		Response response=httprqst.request(Method.DELETE,"/data/3.0/stations"+ep);
		JsonPath jo1=new JsonPath(response.body().asString());	
		Assert.assertEquals(404,response.getStatusCode());
		Assert.assertEquals("Station not found", jo1.get("message"));
		
		ep="/"+id2;
		response=httprqst.request(Method.DELETE,"/data/3.0/stations"+ep);
		JsonPath jo2=new JsonPath(response.body().asString());
		Assert.assertEquals(404,response.getStatusCode());
		Assert.assertEquals("Station not found", jo2.get("message"));
	}
}



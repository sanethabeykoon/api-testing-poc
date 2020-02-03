package io.poc.apit.scenarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.JSONObject;

import io.poc.apit.annotations.RetryCount;
import io.poc.apit.connectors.ConnectionGenerator;
import io.poc.apit.connectors.Connector;
import io.poc.apit.util.Awaiter;

public class ConnectorTest {
	private Connector connector;
	private RequestSpecification connection;
	private Awaiter awaiter;
	private Object[][] postDataSrc = {{ "product", "5522" },
						  	          { "product", "5522" }};
	private Response getResponse, postResponse, putResponse;
	
	@DataProvider(name = "postDataProvider")
	public Object[][] injectPostData() {
		return postDataSrc;
	}
	
	@BeforeClass
	public void setupTest() {
		connection = ConnectionGenerator.getConnection("https://endpoint");
		connector = new Connector();
		awaiter = new Awaiter();
	}
	
	@Test(priority=3, dependsOnMethods = { "testPutRequest" })
	public void testGetRequest() {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("caseId", "12345");
		queryParams.put("product", "678910");
		
		getResponse = connector.getRequest(connection, "/", queryParams);
		Assert.assertEquals(getResponse.getStatusCode(), 200, "GET response is NOT success");
		
		JsonPath responseObj = getResponse.jsonPath();
		
		ArrayList<Object> list = new ArrayList<Object>();
		list = responseObj.get("product");
		
		for(Object item : list){
			String value = item.toString();
			
		    System.out.println(value);
		}
		
		ArrayList<Map<Object, Object>> receipts = new ArrayList<Map<Object, Object>>();
		receipts = responseObj.get("receipt");
				
		for(Map.Entry<Object, Object> entry : receipts.get(0).entrySet()){
			String key = entry.getKey().toString();
		    String value = entry.getValue().toString();
		    
		    System.out.println(key);
		    System.out.println(value);
		}
	}
	
	@Test(priority=1, dataProvider = "postDataProvider")
	public void testPostRequest(String param1, String param2) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("", "");
		headers.put("", "");
						
		JSONObject requestBody = new JSONObject();
		requestBody.put(param1, param2);
		
		postResponse = connector.postRequest(connection, headers, requestBody);
		
		System.out.println(postResponse.getStatusCode());
		System.out.println(postResponse.header("Content-Type"));
		
		Assert.assertEquals(postResponse.getStatusCode(), 400, "POST response is NOT the expected");
		Assert.assertEquals(postResponse.header("Content-Type"), "application/json", "POST response header is NOT the expected");
	}
	
	@Test(priority=2, dependsOnMethods = { "testPostRequest" })
	@RetryCount(3)
	public void testPutRequest() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("", "");
		headers.put("", "");
				
		JSONObject requestBody = new JSONObject();
		requestBody.put("caseId", "123");
		requestBody.put("product", "456");
		requestBody.put("status", "Inprogress");
		
		putResponse = connector.putRequest(connection, headers, requestBody);
		boolean readyState = awaiter.awaitForStatus(putResponse, 1, 15);
		
		System.out.println("Response ready state: " + readyState);
		Assert.assertEquals(readyState, true);
	}
		
	@AfterClass
	public void closeTest() {
		
	}
}

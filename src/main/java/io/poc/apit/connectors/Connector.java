package io.poc.apit.connectors;

import java.util.Map;

import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Connector{

	public RequestSpecification getConnection(String baseUrl) {
		RestAssured.baseURI = baseUrl;
		RequestSpecification connection = RestAssured.given();
		return connection;
	}
	
	public Response getRequest(RequestSpecification connection, String uri, Map<String, String> params) {
		connection.queryParams(params);
		return connection.request(Method.GET, uri);
	}
	
	public Response postRequest(RequestSpecification connection, Map<String, String> headers, JSONObject requestBody) {
		connection.headers(headers);
		connection.body(requestBody.toString());
		return connection.request(Method.POST);
	}
	
	public Response putRequest(RequestSpecification connection, Map<String, String> headers, JSONObject requestBody) {
		connection.headers(headers);
		connection.body(requestBody.toString());
		return connection.request(Method.PUT);
	}
	
	public Response deleteRequest(RequestSpecification connection, Map<String, String> headers, String requestBody) {
		connection.headers(headers);
		connection.body(requestBody);
		return connection.request(Method.DELETE);
	}
}

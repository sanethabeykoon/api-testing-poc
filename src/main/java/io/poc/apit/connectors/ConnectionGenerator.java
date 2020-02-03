package io.poc.apit.connectors;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class ConnectionGenerator {

	public static RequestSpecification getConnection(String baseUrl) {
		RestAssured.baseURI = baseUrl;
		RequestSpecification connection = RestAssured.given();
		return connection;
	}
}

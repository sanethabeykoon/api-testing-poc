package io.poc.apit.util;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.restassured.response.Response;

import static org.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.*;

public class Awaiter {
	
	private Response response;
		
	public boolean awaitForStatus(Response response, long pollInterval, long awaitTime) {
		this.response = response;
		return with().pollInterval(pollInterval, TimeUnit.SECONDS).await().atMost(awaitTime, TimeUnit.SECONDS).until(checkStatus(), equalTo(true));
	}
	
	private Callable<Boolean> checkStatus() {
	      return new Callable<Boolean>() {
	            public Boolean call() throws Exception {
	            	return response.getStatusCode() == 202;
	            }
	      };
	}
}

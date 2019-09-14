package io.poc.apit.util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import io.poc.apit.annotations.RetryCount;

public class RetryAnalyzer implements IRetryAnalyzer{

	private int retryAttempt = 0;
	
	@Override
	public boolean retry(ITestResult result) {

		RetryCount retryAnnotation = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(RetryCount.class);
		
		if((retryAnnotation != null) && (retryAttempt < retryAnnotation.value()))
		{
			retryAttempt++;
			return true;
		}
		return false;
	}
}

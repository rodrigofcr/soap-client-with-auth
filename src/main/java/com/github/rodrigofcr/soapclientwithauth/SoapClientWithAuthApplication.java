package com.github.rodrigofcr.soapclientwithauth;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoapClientWithAuthApplication implements ApplicationRunner {

	private final SoapClient soapClient;

	public SoapClientWithAuthApplication(final SoapClient soapClient) {
		this.soapClient = soapClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(SoapClientWithAuthApplication.class, args).close();
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception {
		soapClient.performRequest();
	}

}

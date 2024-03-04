package com.github.rodrigofcr.soapclientwithauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

@Component
public class SoapClient {

    private final WebServiceTemplate webServiceTemplate;
    private final String emptyEnvelope = "<?xml version=\"1.0\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope/\" soap:encodingStyle=\"http://www.w3.org/2003/05/soap-encoding\"><soap:Header></soap:Header><soap:Body><soap:Fault></soap:Fault></soap:Body></soap:Envelope>";

    @Value("${client.url}")
    private String webServiceUri;

    public SoapClient(final WebServiceTemplateBuilder webServiceTemplateBuilder) {
        this.webServiceTemplate = webServiceTemplateBuilder.build();
    }

    public void performRequest() {
        webServiceTemplate.sendSourceAndReceiveToResult(
                webServiceUri,
                new StreamSource(new StringReader(emptyEnvelope)),
                new StreamResult(System.out));
    }

}

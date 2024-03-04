package com.github.rodrigofcr.soapclientwithauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
public class SoapClientSenderConfig {

    @Value("${certificate.file.path}")
    private String path;

    @Value("${certificate.file.password}")
    private String password;

    @Bean
    public WebServiceMessageSender webServiceMessageSender() {
        final HttpsUrlConnectionMessageSender webServiceMessageSender = new HttpsUrlConnectionMessageSender();
        try {
            final KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
            clientKeyStore.load(
                    new FileInputStream(new File(path)),
                    password.toCharArray());
            final KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            final  KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

            // Assuming that you imported the CA Cert "Subject: CN=MBIIS CA, OU=MBIIS, O=DAIMLER, C=DE"
            // to your cacerts Store.
            final KeyStore trustKeyStore = KeyStore.getInstance("JKS");
            trustKeyStore.load(
                    new FileInputStream("/etc/ssl/certs/java/cacerts"),
                    "changeit".toCharArray());

            final TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustKeyStore);
            final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            SSLContext.setDefault(sslContext);

            webServiceMessageSender.setKeyManagers(keyManagers);
            webServiceMessageSender.setTrustManagers(trustManagers);

        } catch (final KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException |
                       KeyManagementException | UnrecoverableKeyException exception) {
            exception.printStackTrace();
        }

        return webServiceMessageSender;
    }
}

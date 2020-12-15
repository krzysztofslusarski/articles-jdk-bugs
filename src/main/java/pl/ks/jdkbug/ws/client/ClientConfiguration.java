package pl.ks.jdkbug.ws.client;

import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
class ClientConfiguration {
    @Bean
    @SneakyThrows
    HttpClient httpClient() {
        // Do not copy paste that configuration, this is simple config for article purposes.
        RequestConfig requestConfig = RequestConfig.custom()
                .build();

        return HttpClients.custom()
                .addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor())
                .setDefaultRequestConfig(requestConfig)
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    @Bean
    WebServiceProxy<TempClient> documentServiceClient(HttpClient httpClient) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPaths("pl.ks.jdkbug.ws.model");
        return new WebServiceProxy<>(httpClient, TempClient.class, marshaller, marshaller, "https://localhost:8443/ws/temp");
    }

}

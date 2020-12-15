package pl.ks.jdkbug.ws.client;

import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.HackerPoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
class ClientConfiguration {
    @Bean
    @SneakyThrows
    HttpClient httpClient(HackerPoolingHttpClientConnectionManager httpClientConnectionManager) {
        // Do not copy paste that configuration, this is simple config for article purposes.
        RequestConfig requestConfig = RequestConfig.custom()
                .build();

        return HttpClients.custom()
                .addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor())
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(httpClientConnectionManager)
                .build();
    }

    @Bean
    @SneakyThrows
    HackerPoolingHttpClientConnectionManager httpClientConnectionManager() {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.
                <ConnectionSocketFactory> create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();

        HackerPoolingHttpClientConnectionManager connectionPoolManager = new HackerPoolingHttpClientConnectionManager(registry);
        connectionPoolManager.setMaxTotal(10);
        connectionPoolManager.setDefaultMaxPerRoute(10);
        return connectionPoolManager;
    }

    @Bean
    WebServiceProxy<TempClient> documentServiceClient(HttpClient httpClient) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPaths("pl.ks.jdkbug.ws.model");
        return new WebServiceProxy<>(httpClient, TempClient.class, marshaller, marshaller, "https://localhost:8443/ws/temp");
    }

}

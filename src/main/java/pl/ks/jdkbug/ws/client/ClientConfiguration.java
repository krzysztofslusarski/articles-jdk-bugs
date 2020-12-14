package pl.ks.jdkbug.ws.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
class ClientConfiguration {
    @Bean
    WebServiceProxy<TempClient> documentServiceClient() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPaths("pl.ks.jdkbug.ws.model");
        return new WebServiceProxy<>(TempClient.class, marshaller, marshaller, "https://localhost:8443/ws/temp");
    }
}

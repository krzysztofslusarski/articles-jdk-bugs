package pl.ks.jdkbug.ws.server;

import lombok.SneakyThrows;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

@EnableWs
@Configuration
class ServerConfiguration extends WsConfigurerAdapter {
    @Bean
    ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean
    @SneakyThrows
    DefaultWsdl11Definition temp() {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("Temp");
        wsdl11Definition.setLocationUri("/ws/temp");
        wsdl11Definition.setTargetNamespace("http://ks.pl/api");
        SimpleXsdSchema schema = new SimpleXsdSchema(new ClassPathResource("temp.xsd"));
        schema.afterPropertiesSet();
        wsdl11Definition.setSchema(schema);
        return wsdl11Definition;
    }
}

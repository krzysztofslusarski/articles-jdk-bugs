package pl.ks.jdkbug.ws.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Slf4j
@RequiredArgsConstructor
public class WebServiceProxy<T> implements FactoryBean<T>, InitializingBean {
    private final Class<T> serviceInterface;
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;
    private final String defaultUri;

    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    private WebServiceTemplate webServiceTemplate;
    private T proxy;

    public void afterPropertiesSet() {
        this.proxy = (T) new ProxyFactory(serviceInterface, createMethodInterceptor()).getProxy(classLoader);

        if (webServiceTemplate != null) {
            return;
        }

        webServiceTemplate = new WebServiceTemplate();
        SaajSoapMessageFactory factory = new SaajSoapMessageFactory();
        factory.setSoapVersion(SoapVersion.SOAP_11);
        factory.afterPropertiesSet();
        webServiceTemplate.setMessageFactory(factory);
        webServiceTemplate.setDefaultUri(defaultUri);
        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(unmarshaller);
        webServiceTemplate.afterPropertiesSet();
    }

    public T getObject() {
        return proxy;
    }

    public Class<T> getObjectType() {
        return serviceInterface;
    }

    public boolean isSingleton() {
        return true;
    }

    private MethodInterceptor createMethodInterceptor() {
        return this::invokeMethod;
    }

    private Object invokeMethod(MethodInvocation methodInvocation) throws IllegalArgumentException {
        Object arg = methodInvocation.getArguments()[0];
        return webServiceTemplate.marshalSendAndReceive(arg);
    }
}

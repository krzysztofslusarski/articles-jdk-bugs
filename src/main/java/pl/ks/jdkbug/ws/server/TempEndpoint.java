package pl.ks.jdkbug.ws.server;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.ks.jdkbug.ws.model.TempRequest;
import pl.ks.jdkbug.ws.model.TempResponse;

@Slf4j
@Endpoint
@RequiredArgsConstructor
class TempEndpoint {
    private static final String NAMESPACE_URI = "http://ks.pl/api";

    private final HttpServletResponse response;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TempRequest")
    @ResponsePayload
    TempResponse temp(@RequestPayload TempRequest request) {
        response.addHeader("Connection", "close");
        return new TempResponse();
    }
}

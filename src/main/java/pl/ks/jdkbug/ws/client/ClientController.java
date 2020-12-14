package pl.ks.jdkbug.ws.client;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.ks.jdkbug.ws.model.TempRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
class ClientController {
    private final TempClient tempClient;

    @SneakyThrows
    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    void get(HttpServletResponse response) {
        log.info("Invoking WS");
        tempClient.temp(new TempRequest());
        log.info("WS Invoked");
        response.getWriter().write("WS Invoked");
        response.getWriter().flush();
    }
}

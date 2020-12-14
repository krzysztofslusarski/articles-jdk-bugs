package pl.ks.jdkbug.ws.client;

import pl.ks.jdkbug.ws.model.TempRequest;
import pl.ks.jdkbug.ws.model.TempResponse;

public interface TempClient {
    TempResponse temp(TempRequest request);
}

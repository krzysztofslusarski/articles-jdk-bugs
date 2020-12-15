package org.apache.http.impl.conn;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.logging.Log;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.impl.io.SessionOutputBufferImpl;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

class HackerLoggingManagedHttpClientConnection extends LoggingManagedHttpClientConnection {
    public HackerLoggingManagedHttpClientConnection(String id, Log log, Log headerLog, Log wireLog, int bufferSize, int fragmentSizeHint, CharsetDecoder charDecoder, CharsetEncoder charEncoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super(id, log, headerLog, wireLog, bufferSize, fragmentSizeHint, charDecoder, charEncoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);
    }

    @Override
    public void close() throws IOException {
        try {
            Class<?> superclass = this.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass();
            Field socketHolderField = superclass.getDeclaredField("socketHolder");
            Field inBufferField = superclass.getDeclaredField("inBuffer");
            Field outbufferField = superclass.getDeclaredField("outbuffer");
            socketHolderField.setAccessible(true);
            inBufferField.setAccessible(true);
            outbufferField.setAccessible(true);
            AtomicReference<Socket> socketHolder = (AtomicReference<Socket>) socketHolderField.get(this);
            SessionInputBufferImpl inBuffer = (SessionInputBufferImpl) inBufferField.get(this);
            SessionOutputBufferImpl outbuffer = (SessionOutputBufferImpl) outbufferField.get(this);
            final Socket socket = socketHolder.getAndSet(null);
            if (socket != null) {
                try {
                    inBuffer.clear();
                    outbuffer.flush();
                    try {
                        try {
                            socket.shutdownOutput();
                        } catch (final IOException ignore) {
                        }
                        Method method = socket.getClass().getDeclaredMethod("shutdownInput", boolean.class);
                        method.setAccessible(true);
                        method.invoke(socket, false);
                    } catch (final UnsupportedOperationException ignore) {
                        // if one isn't supported, the other one isn't either
                    }
                } finally {
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            super.close();
        }
    }
}

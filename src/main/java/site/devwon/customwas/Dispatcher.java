package site.devwon.customwas;

import site.devwon.customwas.request.HttpRequest;
import site.devwon.customwas.response.HttpResponse;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

public class Dispatcher {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Dispatcher.class);

    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public Dispatcher(HttpRequest httpRequest, HttpResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public void dispatch() throws ParseException {
        logger.debug("requestLine : {}", httpRequest.getPath());
        HandlerMapping.findController(httpRequest.getPath()).service(httpRequest, httpResponse);
    }
}

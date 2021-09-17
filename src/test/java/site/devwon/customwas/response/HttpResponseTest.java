package site.devwon.customwas.response;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class HttpResponseTest {
    private HttpResponse httpResponse;
    private String testDirectory = "./src/main/resources/webapp/test/";

    @Test
    public void sendRedirect() throws FileNotFoundException {
        httpResponse = HttpResponseFactory.createHttpResponse(createOutputStream("HTTP_REDIRECT.txt"));
        httpResponse.sendRedirect("/index.html");
    }

    @Test
    public void forbidden() throws FileNotFoundException {
        httpResponse = HttpResponseFactory.createHttpResponse(createOutputStream("HTTP_FORBIDDEN.txt"));
        httpResponse.forbidden("/index.html");
    }

    @Test
    public void notFound() throws FileNotFoundException {
        httpResponse = HttpResponseFactory.createHttpResponse(createOutputStream("HTTP_NOT_FOUND.txt"));
        httpResponse.notFound("/index.html");
    }

    @Test
    public void error() throws FileNotFoundException {
        httpResponse = HttpResponseFactory.createHttpResponse(createOutputStream("HTTP_ERROR.txt"));
        httpResponse.error("/index.html");
    }

    @Test
    public void process() throws FileNotFoundException {
        httpResponse = HttpResponseFactory.createHttpResponse(createOutputStream("HTTP_PROCESS.txt"));
        httpResponse.process("/index.html");
    }

    @Test
    public void processBody() throws FileNotFoundException {
        httpResponse = HttpResponseFactory.createHttpResponse(createOutputStream("HTTP_PROCESS_BODY.txt"));
        httpResponse.processBody("/index.html");
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
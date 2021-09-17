package site.devwon.customwas.response;

import ch.qos.logback.classic.Logger;
import site.devwon.customwas.HttpServer;
import site.devwon.customwas.constant.HttpStatusCode;
import site.devwon.customwas.constant.HttpVersion;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponse {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HttpResponse.class);
    private static final String LOCATION = "Location";
    private static final String CONTENT_LENGTH = "Content-Length";
    private StatusLine statusLine;
    private ResponseHeader responseHeader;
    private ResponseBody responseBody;
    private DataOutputStream dataOutputStream;

    public HttpResponse(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
        responseHeader = new ResponseHeader();
    }

    private void addStatusLine(String httpVersion, HttpStatusCode statusCode) {
        statusLine = new StatusLine(httpVersion, statusCode);
    }

    private void addResponseBody(String url) {
        try {
            this.responseBody = new ResponseBody(Files.readAllBytes(Paths.get("./src/main/resources/webapp" + url)));
        } catch (IOException e) {
            notFound(url);
        }
    }

    private void addResponseDynamicBody(String dynamicResource) {
        this.responseBody = new ResponseBody(dynamicResource.getBytes());
    }

    public void sendRedirect(String url) {
        addStatusLine(HttpVersion.HTTP_VERSION_1_1, HttpStatusCode.FOUND);
        addHeader(LOCATION, url);
        writeResponseMessage();
    }

    public void forbidden(String url) {
        addStatusLine(HttpVersion.HTTP_VERSION_1_1, HttpStatusCode.FORBIDDEN);
        addHeader(LOCATION, url);
        addResponseDynamicBody(makeHtmlByCode(HttpStatusCode.FORBIDDEN.getHttpStatusNumber().substring(0, 3)));
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    public void notFound(String url) {
        addStatusLine(HttpVersion.HTTP_VERSION_1_1, HttpStatusCode.NOT_FOUND);
        addHeader(LOCATION, url);
        addResponseDynamicBody(makeHtmlByCode(HttpStatusCode.NOT_FOUND.getHttpStatusNumber().substring(0, 3)));
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    public void error(String url) {
        addStatusLine(HttpVersion.HTTP_VERSION_1_1, HttpStatusCode.ERROR);
        addHeader(LOCATION, url);
        addResponseDynamicBody(makeHtmlByCode(HttpStatusCode.ERROR.getHttpStatusNumber().substring(0, 3)));
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    private void writeResponseMessage() {
        try {
            statusLine.addWriteStatusLine(dataOutputStream);
            responseHeader.addWriteHeader(dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(String filePath) {
        addStatusLine(HttpVersion.HTTP_VERSION_1_1, HttpStatusCode.OK);
        logger.debug("filePath : {}", filePath);
        addResponseBody(filePath);

        addHeader(CONTENT_LENGTH, responseBody.getBodyLength());
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    public void processBody(String dynamicResource) {
        addStatusLine(HttpVersion.HTTP_VERSION_1_1, HttpStatusCode.OK);
        addResponseDynamicBody(dynamicResource);
        addHeader(CONTENT_LENGTH, responseBody.getBodyLength());
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    public void addHeader(String header, String value) {
        responseHeader.addHeader(header, value);
    }

    public String makeHtmlByCode(String HttpCode) {
        String JsonValue = "";
        StringBuffer sb = new StringBuffer();

        if (HttpServer.HtmlStatusCodeArray != null)
            for (int i = 0; i < HttpServer.HtmlStatusCodeArray.size(); i++) {
                JSONObject jsonObj2 = (JSONObject) HttpServer.HtmlStatusCodeArray.get(i);

                if ((String) jsonObj2.get(HttpCode) != null) {
                    JsonValue = (String) jsonObj2.get(HttpCode);
                    sb.append("<HTML>\r\n");
                    sb.append("<HEAD><TITLE> " + JsonValue + " </TITLE>\r\n");
                    sb.append("</HEAD>\r\n");
                    sb.append("<BODY>\r\n");
                    sb.append("<H1>HTTP Error " + HttpCode + " " + JsonValue + " </H1>\r\n");
                    sb.append("</BODY>\r\n");
                    sb.append("</HTML>\r\n");
                    sb.append("<HTML>\r\n");
                }

            }
        return sb.toString();
    }
}

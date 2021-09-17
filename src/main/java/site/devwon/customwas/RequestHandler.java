package site.devwon.customwas;

import ch.qos.logback.classic.Logger;
import site.devwon.customwas.constant.FileExtension;
import site.devwon.customwas.constant.HttpMethod;
import site.devwon.customwas.request.HttpRequest;
import site.devwon.customwas.request.HttpRequestFactory;
import site.devwon.customwas.response.HttpResponse;
import site.devwon.customwas.response.HttpResponseFactory;
import site.devwon.customwas.util.HttpRequestUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.ParseException;

public class RequestHandler implements Runnable {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(RequestHandler.class);
    private File rootDirectory;
    private String documentDirectory;
    private String indexFileName;
    private Socket connection;

    public RequestHandler(File rootDirectory, String documentDirectory, String indexFileName, Socket connection) {
        if (rootDirectory.isFile()) {
            throw new IllegalArgumentException("rootDirectory must be a directory, not a file");
        }

        try {
            rootDirectory = rootDirectory.getCanonicalFile();
        } catch (IOException e) {
            logger.error("RequestHandler init {} ", e);
        }
        if (indexFileName != null)
            this.indexFileName = indexFileName;
        this.rootDirectory = rootDirectory;
        this.documentDirectory = documentDirectory;
        this.connection = connection;
    }

    @Override
    public void run() {
        logger.debug("NewClientConnect!ConnectedIP:{},Port:{}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequestFactory.createHttpRequest(in);
            HttpResponse httpResponse = HttpResponseFactory.createHttpResponse(out);

            checkUpperDirectoryFileExtension(httpRequest, httpResponse);

            Dispatcher dispatcher = new Dispatcher(httpRequest, httpResponse);
            dispatcher.dispatch();

        } catch (IOException | ParseException e) {
            logger.error(String.valueOf(e.getStackTrace()));
        }
    }

    private void checkUpperDirectoryFileExtension(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
//        logger.info("RemoteSocketAddress {}", connection.getRemoteSocketAddress());
        logger.debug("RemoteSocketAddress {}", connection.getRemoteSocketAddress());

        // html 파일 경로 /webapp 추가.
        String documentDirectoryPath = rootDirectory.getPath() + documentDirectory;
        String fileName = httpRequest.getPath();

        if (HttpMethod.GET.equals(httpRequest.getMethod())) {
            if (fileName.endsWith(File.separator)) fileName += indexFileName;
            File file = new File(documentDirectoryPath, fileName.substring(1, fileName.length()));

            // 상위 경로 접근, 403 forbidden 처리
            if (HttpRequestUtils.getRootSeparatorCount(file.getAbsolutePath()) < HttpRequestUtils.getRootSeparatorCount(rootDirectory.getAbsolutePath())) {
                httpResponse.forbidden(fileName);
            }

            // fileName의 확장자명이 .exe 일 경우 403 forbidden
            String[] result = fileName.split("\\.");
            String extension = result[result.length - 1];

            if (FileExtension.exe.equals(extension))
                httpResponse.forbidden(fileName);

            if (file.getCanonicalPath().startsWith(documentDirectoryPath)) {
                // Dispatcher 가 처리
            } else {
                // not found Directory file
                httpResponse.notFound(fileName);
            }
        } else {
            httpResponse.error(fileName);
        }

    }
}
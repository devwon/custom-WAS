package site.devwon.customwas;

import ch.qos.logback.classic.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class HttpServer {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HttpServer.class);

    // 프로젝트 상위 경로, index 파일명 설정
    private static final String INDEX_FILE = File.separator + "index.html";
    private final File rootDirectory;

    private static int port;
    private static int numThreads;
    private final ExecutorService pool;
    private static String documentDirectory;

    public static JSONArray HtmlStatusCodeArray;
    public static JSONArray ControllerMapper;

    public HttpServer(File rootDirectory, String projectDirectory) throws IOException {
        // json 파일 설정
        settingJson(projectDirectory);

        this.pool = Executors.newFixedThreadPool(numThreads);
        this.rootDirectory = rootDirectory;

        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory + " does not exist as a directory");
        }
    }

    public void start() throws IOException {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName("localhost"))) {
//            logger.info("HostName:{}, InetAddress:{}, Port:{}, DocumentRoot:{}",
            logger.debug("HostName:{}, InetAddress:{}, Port:{}, DocumentRoot:{}",
                    InetAddress.getLocalHost().getHostName(), server.getInetAddress(), server.getLocalPort(), rootDirectory);
            while (true) {
                try {
                    Socket connection = server.accept();
                    Runnable r = new RequestHandler(rootDirectory, documentDirectory, INDEX_FILE, connection);
                    pool.submit(r);
                } catch (IOException ex) {
                    logger.error("Level:{} Error accepting connection Exception:{}", Level.WARNING, ex);
                }
            }
        }
    }

    private static void settingJson(String rootDirectory) {
        String jsonDirectory = rootDirectory + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "properties.json";
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonDirectory));

            try {
                port = Integer.parseInt((String) jsonObject.get("PORT"));
                numThreads = Integer.parseInt((String) jsonObject.get("NUM_THREADS"));
                if (port < 0 || port > 65535) port = 8000;
                documentDirectory = (String) jsonObject.get("ROOT");
            } catch (RuntimeException ex) {
                port = 8000;
                documentDirectory = File.separator + "webapp";
            }

            // casting to A JSON array
            HtmlStatusCodeArray = (JSONArray) jsonObject.get("HTTP_STATUS_CODE_LIST");
            ControllerMapper = (JSONArray) jsonObject.get("CONTROLLER_MAPPER");
        } catch (Exception e) {
            logger.error("settingJson {} ", e);
        }
    }

    public static void main(String[] args) {

        File rootDirectory;
        String projectDirectory;

        if (args[0] == null)
            throw new RuntimeException("명령줄에 프로젝트 최상위 경로를 입력해주세요(e.g. /Users/wonhyerin/nhn)");
        else
            projectDirectory = args[0];

        try {
            rootDirectory = new File(projectDirectory);
        } catch (ArrayIndexOutOfBoundsException ex) {
            logger.error("Usage: java JHTTP docroot port {} ", ex);
            return;
        }
        try {
            HttpServer httpServer = new HttpServer(rootDirectory, projectDirectory);
            httpServer.start();
        } catch (IOException ex) {
            logger.error("Level:{} Server could not start Exception:{}", Level.SEVERE, ex);
        }
    }
}

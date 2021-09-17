package site.devwon.customwas.util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * @param start point of Request Body
     * @param contentLength
     * @return body
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}

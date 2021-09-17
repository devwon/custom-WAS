package site.devwon.customwas.util;

import site.devwon.customwas.request.RequestBody;
import site.devwon.customwas.request.RequestHeader;
import site.devwon.customwas.request.RequestLine;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HttpRequestUtils {
    /**
     * @param queryString
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, "; ");
    }

    /**
     * @param directoryRoot(\ in Windows, / in Mac)
     * @return RootSeparatorCount
     */
    public static int getRootSeparatorCount(String directoryRoot) {
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        String[] tokens = directoryRoot.split(pattern);
        return tokens.length - 1;
    }

    /**
     * @param values
     * @param separator ex) &,;
     * @return parsed Map
     */
    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
    }

    /**
     * @param keyValue
     * @param separator ex) &,;
     * @return parsed Map
     */
    static Pair getKeyValue(String keyValue, String separator) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(separator);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static RequestHeader createRequestHeader(BufferedReader br) throws IOException {
        String line;
        Map<String, String> headers = new HashMap<>();
        while (!(line = br.readLine()).isEmpty()) {
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
        }
        return new RequestHeader(headers);
    }

    public static RequestBody createRequestBody(BufferedReader br, int contentLength) throws IOException {
        if (contentLength == 0) {
            return null;
        }
        String body = IOUtils.readData(br, contentLength);
        return new RequestBody(body);
    }

    public static RequestLine createRequestLine(BufferedReader br) throws IOException {
        String[] requestLine = br.readLine().split(" ");
        return new RequestLine(requestLine[0], requestLine[1], requestLine[2]);
    }
}

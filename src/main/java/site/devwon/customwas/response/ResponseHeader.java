package site.devwon.customwas.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseHeader {
    private Map<String, String> headers = new HashMap<>();

    public void addWriteHeader(DataOutputStream dos) throws IOException {
        for (String key : headers.keySet()) {
            dos.writeBytes(String.format("%s: %s\r\n", key, headers.get(key)));
        }
        dos.writeBytes("\r\n");
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }
}

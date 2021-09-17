package site.devwon.customwas;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class HttpServerTest {

    @Test
    public void settingJson() throws IOException, ParseException {
        String jsonDirectory = "./src/test/resources/properties.json";
        JSONParser parser = new JSONParser();
        int port;
        String root;

        Object obj = parser.parse(new FileReader(jsonDirectory));
        JSONObject jsonObject = (JSONObject) obj;

        port = Integer.parseInt((String) jsonObject.get("PORT"));
        assertThat(port, is(8000));

        root = (String) jsonObject.get("ROOT");
        assertThat(root, is("/src/main/resources/webapp"));
    }
}
package nz.ac.wgtn.swen301.resthome4logs.server;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestStatsHTML {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    LogsServlet servlet;
    StatsServlet statsServlet;
    Persistency persistency;
    ArrayList<String> levels;

    @Before
    public void init() throws ServletException, IOException {
        servlet = new LogsServlet();
        statsServlet = new StatsServlet();
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        persistency = new Persistency();
        levels = persistency.getLevels();
        generateLogs();
    }

    public void generateLogs() throws ServletException, IOException {
        for(int i = 0; i < 5; i++){
            JSONObject json = makeJSONObject("htmlTest" + i, levels.get(i));
            request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));
            servlet.doPost(request, response);
        }
    }

    @Test
    public void testValidHTMLresponse() throws IOException, ServletException {
        StatsServlet service = new StatsServlet();
        service.doGet(request, response);
        assert response.getStatus() == 200;
    }

    @Test
    public void testValidHTML() throws IOException, ServletException {
        statsServlet.doGet(request, response);
        String html = response.getContentAsString();
        System.out.println(html);
        String[] content = html.split("\n");

        int logs = 0;
        //Nested for loop to check each cell
        for (int k = 1; k < content.length; k++) {
            String[] line = content[k].split("\t");
            for (int j = 1; j < line.length; j++) {
                logs += Integer.parseInt(line[j]);
            }
        }

        int s = persistency.getDB().size();
        assert persistency.getDB().size() == logs;
    }

    public JSONObject makeJSONObject(String message, String level) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        jsonObject.put("message", message);
        jsonObject.put("timestamp", (LocalDateTime.now()));
        jsonObject.put("thread", "main");
        jsonObject.put("logger", "com.example.Foo");
        jsonObject.put("level", level);
        jsonObject.put("errorDetails", "string");

        return jsonObject;
    }
}

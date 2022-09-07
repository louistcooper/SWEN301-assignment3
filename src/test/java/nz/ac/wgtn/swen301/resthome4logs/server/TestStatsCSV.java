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

public class TestStatsCSV {
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    LogsServlet servlet;
    Persistency persistency;
    ArrayList<String> levels;

    @Before
    public void init() throws ServletException, IOException {
        servlet = new LogsServlet();
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        persistency = new Persistency();
        levels = persistency.getLevels();
        generateMultipleLogs();

    }

    public void generateMultipleLogs() throws ServletException, IOException {
        for(int k = 0; k < 5; k++){
            JSONObject json = makeJSONObject("csvTest" + k, levels.get(k));
            request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));
            servlet.doPost(request, response);
        }
    }

    @Test
    public void testCSVdoGet() throws ServletException, IOException {
        StatsCSVServlet csvServlet = new StatsCSVServlet();
        csvServlet.doGet(request, response);

        String header = "attachment; filename = statscsv.csv";
        assertEquals(header, response.getHeaderValue("content-disposition"));
    }

    @Test
    public void testValidCSVResponse() throws IOException, ServletException {
        StatsCSVServlet csvServlet = new StatsCSVServlet();
        csvServlet.doGet(request, response);
        assert response.getStatus() == 200;
    }

    @Test
    public void testValidCSV() throws IOException, ServletException {
        StatsCSVServlet service = new StatsCSVServlet();
        service.doGet(request, response);
        //assert response.getContentAsString().startsWith("Logger\tALL\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tTRACE\tOFF\n");
        String[] content = response.getContentAsString().split("\n");

        int logs = 0;
        //Nested for loop to check each cell
        for (int i = 1; i < content.length; i++) {
            String[] line = content[i].split("\t");
            for (int j = 1; j < line.length; j++) {
                logs += Integer.parseInt(line[j]);
            }
        }
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

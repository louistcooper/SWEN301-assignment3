package nz.ac.wgtn.swen301.resthome4logs.server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class TestPostLogs {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    LogsServlet servlet;
    Persistency persistency;

    @Before
    public void setup() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.servlet = new LogsServlet();
        this.persistency = new Persistency();
    }

    @Test
    public void testDuplicateJson() {

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        jsonObject1.put("message", "application started");
        jsonObject1.put("timestamp", "123");
        jsonObject1.put("thread", "main");
        jsonObject1.put("logger", "com.example.Foo");
        jsonObject1.put("level", "DEBUG");
        jsonObject1.put("errorDetails", "string");

        request.setContent(jsonObject1.toString().getBytes(StandardCharsets.UTF_8));

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        jsonObject2.put("message", "application started");
        jsonObject2.put("timestamp", "123");
        jsonObject2.put("thread", "main");
        jsonObject2.put("logger", "com.example.Foo");
        jsonObject2.put("level", "DEBUG");
        jsonObject2.put("errorDetails", "string");

        request.setContent(jsonObject2.toString().getBytes(StandardCharsets.UTF_8));

        try {
            servlet.doPost(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        assertEqualsInt(409,response.getStatus());
    }

    @Test
    public void testInvalidJson() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "");
        jsonObject.put("message", "");
        jsonObject.put("timestamp", (LocalDateTime.now()));
        jsonObject.put("thread", "");
        jsonObject.put("logger", "");
        jsonObject.put("level", "");
        jsonObject.put("errorDetails", "");

        request.setContent(jsonObject.toString().getBytes(StandardCharsets.UTF_8));

        try {
            servlet.doPost(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        assertEqualsInt(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    private void assertEqualsInt(int i, int status) {
    }
}

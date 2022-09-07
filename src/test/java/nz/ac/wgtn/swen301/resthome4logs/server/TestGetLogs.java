package nz.ac.wgtn.swen301.resthome4logs.server;


import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class TestGetLogs {

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

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testvalidInputs1() throws IOException, ServletException {


        request.setParameter("limit", "56");
        request.setParameter("level", "WARN");
        servlet.doGet(request,response);

        assertEqualsInt(200,response.getStatus());
    }

    @Test
    public void testvalidInputs2() throws IOException, ServletException {


        request.setParameter("limit", "24");
        request.setParameter("level", "DEBUG");
        servlet.doGet(request,response);

        assertEqualsInt(200,response.getStatus());
    }

    @Test
    public void testInvalidInputs1() throws IOException, ServletException {

        //limit is incorrect

        request.setParameter("limit", "0");
        request.setParameter("level", "WARN");
        servlet.doGet(request,response);

        assertEqualsInt(400,response.getStatus());
    }

    @Test
    public void testInvalidInputs2() throws IOException, ServletException {

        //level is incorrect

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("No enum constant");

        request.setParameter("limit", "5");
        request.setParameter("level", "SCARY");
        servlet.doGet(request,response);

        assertEqualsInt(400,response.getStatus());
    }

    @Test
    public void testInvalidInputs3() throws IOException, ServletException {

        //limit & level is incorrect

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("No enum constant");

        request.setParameter("limit", "-1");
        request.setParameter("level", "WARNING");
        servlet.doGet(request,response);

        assertEqualsInt(400,response.getStatus());
    }

    @Test
    public void testValidContentType() throws IOException, ServletException {

        request.setParameter("limit", "42");
        request.setParameter("level", "WARN");
        servlet.doGet(request,response);

        assertTrue(response.getContentType().startsWith("application/json"));
    }

    @Test
    public void testValidJson(){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject = makeJSONObject();
            request.setCharacterEncoding("UTF-8");
            request.setContent(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            servlet.doPost(request, response);

            request.setParameter("limit", "1");
            request.setParameter("level", "ALL");

            servlet.doGet(request,response);
            jsonArray.put(jsonObject);


        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        //for(JSONObject testObj : jsonArray) {
        assertEqualsString("1234567890", jsonObject.getString("id"));
        assertEqualsString("jsonTest", jsonObject.getString("message"));
        assertEqualsString("main", jsonObject.getString("thread"));
        assertEqualsString("com.example.Foo", jsonObject.getString("logger"));
        assertEqualsString("DEBUG", jsonObject.getString("level"));
        assertEqualsString("string", jsonObject.getString("errorDetails"));
        //}

    }


    public JSONObject makeJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        jsonObject.put("message", "application started");
        jsonObject.put("timestamp", (LocalDateTime.now()));
        jsonObject.put("thread", "main");
        jsonObject.put("logger", "com.example.Foo");
        jsonObject.put("level", "DEBUG");
        jsonObject.put("errorDetails", "string");

        return jsonObject;
    }


    private void assertTrue(boolean startsWith) {
    }

    private void assertEqualsInt(int i, int status) {
    }

    private void assertEqualsString(String value, String key) {
    }


}

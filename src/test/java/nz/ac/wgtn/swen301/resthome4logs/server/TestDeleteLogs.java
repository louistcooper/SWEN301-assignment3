package nz.ac.wgtn.swen301.resthome4logs.server;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestDeleteLogs {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    LogsServlet servlet;

    @Before
    public void setup() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.servlet = new LogsServlet();
    }

    @Test
    public void testDeleteReturnsErrorcode() throws IOException, ServletException {
        servlet.doDelete(request,response);

        assertEquals(200, response.getStatus());
    }
}

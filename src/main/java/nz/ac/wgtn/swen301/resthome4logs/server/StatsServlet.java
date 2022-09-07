package nz.ac.wgtn.swen301.resthome4logs.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class StatsServlet extends HttpServlet {

    public StatsServlet(){}

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTable(req, resp);
    }

    private void processTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        Persistency persistency = new Persistency();
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Stats Servlet logs</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Stats Servlet logs</h1>");
        out.println("<table border><th>Logger</th><th>ALL</th><th>DEBUG</th>" +
                "<th>INFO</th><th>WARN</th><th>ERROR</th><th>FATAL</th><th>TRACE</th><th>OFF</th>");
        // list headers send by the client
        LinkedHashMap<String, int[]> levelsFrequency = persistency.getLogLevels();
        for(String logger : levelsFrequency.keySet()) {
            out.print("<tr><td>");
            out.print(logger);
            out.print("<td>");
            for(int i = 0; i <= 7; i++){
                out.print(levelsFrequency.get(logger)[i]);
                out.print("</td>");
                out.print("<td>");
            }
        }
        out.println("</td></tr>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}

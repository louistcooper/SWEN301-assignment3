package nz.ac.wgtn.swen301.resthome4logs.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class StatsCSVServlet extends HttpServlet{
    public StatsCSVServlet(){}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/csv");
        resp.addHeader("content-disposition", "attachment; filename = statscsv.csv");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
             resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        Persistency persistency = new Persistency();

        if (out != null) {
            out.print("Logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF\n");
            //for(int i = 0; i < Persistency.DB.size(); i++) {
            LinkedHashMap<String, int[]> levelsFrequency = persistency.getLogLevels();
            //}

            for (String logger : levelsFrequency.keySet()) {
                out.print(logger + "\t");
                for (Integer count : levelsFrequency.get(logger)) {
                    out.print(count.toString() + "\t");
                }
                out.print("\n");
            }
            out.flush();
        }
        out.close();
    }
}

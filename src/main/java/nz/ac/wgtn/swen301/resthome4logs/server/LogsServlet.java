package nz.ac.wgtn.swen301.resthome4logs.server;

import org.apache.log4j.spi.LoggingEvent;
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
import java.util.ArrayList;
import java.util.List;

public class LogsServlet extends HttpServlet {
    public LogsServlet(){
        super();
        new Persistency();
    }

    public enum Level{
        ALL,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL,
        TRACE,
        OFF
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String limit = req.getParameter("limit");
        String level = req.getParameter("level");

        if(!level.equals(Level.values())){
            System.out.println("Enter in correct level");
        }


        int limitInt = Integer.parseInt(limit);
        int levelInt = Level.valueOf(level).ordinal();

        PrintWriter out = resp.getWriter();

        if(!Persistency.DB.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < Persistency.DB.size(); i++){
                if(i >= limitInt){
                    break;
                }
                JSONObject obj = Persistency.DB.get(i);
                String objLevel = obj.getString("level");
                int objLevelInt = Level.valueOf(objLevel).ordinal();
                if(objLevelInt <= levelInt){
                    jsonArray.put(obj);
                }
            }
            out.println(jsonArray.toString(4));
            out.close();
        } else {
            out.println(new JSONArray());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        JSONObject jsonObject = (JSONObject) new JSONTokener(reader).nextValue();

        Persistency.DB.add(jsonObject);
        //System.out.println(jsonObject.toString(4));


        //super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Persistency.DB.clear();
    }

}

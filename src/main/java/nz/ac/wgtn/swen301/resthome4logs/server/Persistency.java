package nz.ac.wgtn.swen301.resthome4logs.server;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Persistency {

    public static List<JSONObject> DB = new ArrayList<>();
    private static String[] levels = null;
    public Persistency()
    {
        DB = new ArrayList<>();
        levels = new String[]{"ALL", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "TRACE", "OFF"};
    }

    public List getDB() {
        return DB;
    }

    public void setDB(List DB) {
        this.DB = DB;
    }

    public LinkedHashMap<String, int[]> getLogLevels(){

        /**
         * for each log, add to a map of string and a list. The list contains a number of all levels
         */

        LinkedHashMap<String, int[]> levelsFrequency = new LinkedHashMap<>();

        for(int i = 0; i < DB.size(); i++){
            JSONObject jsonObject = DB.get(i);
            levelsFrequency.put(jsonObject.getString("logger"), new int[7]);
        }

        //jsonObject.getString("id")

        for(JSONObject jsonObject : DB){
            for(int i = 0; i < 7; i++){
                if(jsonObject.getString("logger").equals(levels[i])) {
                    levelsFrequency.get(jsonObject.getString("logger"))[i]++;
                }
            }
        }

        return levelsFrequency;
    }

    public ArrayList<String> getLevels(){
        return new ArrayList<>(Arrays.asList(levels));
    }


}

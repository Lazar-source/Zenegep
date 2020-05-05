package com.example.zenegep;

import java.util.HashMap;
import java.util.Map;

public class ClientClassActivity {
    private String IP_cim;
    public static Map<String,Boolean> bekuldottzenek=new HashMap<>();
    public static Map<String,Boolean> torlendozenek=new HashMap<>();
    public static Map<String,Integer> statisticMap=new HashMap<>();
    public ClientClassActivity(String ip_cim)
    {
        IP_cim=ip_cim;
    }

    public String getIP_cim() {
        return IP_cim;
    }

    public  Map<String, Boolean> getBekuldottzenek() {
        return bekuldottzenek;
    }

    public  Map<String, Boolean> getTorlendozenek() {
        return torlendozenek;
    }

    public  void addBekuldottzenek(String videoID) {
        bekuldottzenek.put(videoID,true);
    }

    public  void addTorlendozenek(String videoID) {
        torlendozenek.put(videoID,true);
    }
    public Boolean AlreadyBekuldottzenek(String videoID)
    {
        boolean already=false;
        for(String s: bekuldottzenek.keySet())
        {
            if (s.equals(videoID))
            {
                already=true;
            }
        }
        return already;

    }
    public Boolean AlreadyTorlendozenek(String videoID)
    {
        boolean already=false;
        for(String s: torlendozenek.keySet())
        {
            if (s.equals(videoID))
            {
                already=torlendozenek.get(s);
            }
        }
        return already;

    }
    public void DeleteZene(String videoID)
    {
        bekuldottzenek.remove(videoID);
        torlendozenek.remove(videoID);
    }

    public  Map<String, Integer> getStatisticMap() {
        return statisticMap;
    }

    public  void setStatisticMap(Map<String,Integer> statisticMap) {
        ClientClassActivity.statisticMap = statisticMap;
    }
}

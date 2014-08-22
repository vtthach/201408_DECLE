package com.example.dragtofillcolor;

import java.util.ArrayList;

public class AreaData {
    private AreaData(){
        
    }
    
    public static AreaData getInstance(){
        return instance;
    }
    
    static AreaData instance = new AreaData();
    public ArrayList<Polygon> list = new ArrayList<>();
}

package com.signomix.events;

import java.util.HashMap;
import org.cricketmsf.event.Event;

public class NewVirtualData extends Event {

    private HashMap<String,String> data = null;

    public NewVirtualData() {
        super();
        data=new HashMap<>();
    }
    public NewVirtualData(String origin, String payload) {
        super();
        data=new HashMap<>();
        data.put("origin", origin);
        data.put("payload", payload);
    }
    
    public NewVirtualData data(HashMap<String,String> data){
        this.data=data;
        return this;
    }

    /**
     * @return the data
     */
    @Override
    public HashMap<String,String> getData() {
        return data;
    }

}

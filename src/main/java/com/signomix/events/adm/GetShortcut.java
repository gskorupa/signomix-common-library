package com.signomix.events.adm;

import java.util.HashMap;
import org.cricketmsf.event.Event;

public class GetShortcut extends Event {

    private HashMap<String,String> data = null;

    public GetShortcut() {
        super();
        data=new HashMap<>();
    }
    
    public GetShortcut data(HashMap<String,String> data){
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

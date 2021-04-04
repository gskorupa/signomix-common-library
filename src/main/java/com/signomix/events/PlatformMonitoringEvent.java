
package com.signomix.events;

import java.util.HashMap;
import org.cricketmsf.event.Event;

public class PlatformMonitoringEvent extends Event {
    
    private HashMap<String,Object> data=null;

    public PlatformMonitoringEvent() {
        super();
    }
    
    public PlatformMonitoringEvent data(HashMap<String,Object> newData){
        this.data=newData;
        return this;
    }

    /**
     * @return the data
     */
    @Override
    public HashMap<String,Object> getData() {
        return data;
    }
    
}

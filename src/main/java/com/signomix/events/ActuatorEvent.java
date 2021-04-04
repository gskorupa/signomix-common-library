
package com.signomix.events;
import java.util.HashMap;
import org.cricketmsf.event.Event;

public class ActuatorEvent extends Event {
    
    private HashMap<String,Object> data=null;

    public ActuatorEvent() {
        super();
    }
    
    public ActuatorEvent(String origin, String payload, String hexPayload) {
        super();
        data.put("origin", origin);
        data.put("payload", payload);
        data.put("hexpayload", hexPayload);
    }
    
    public ActuatorEvent data(HashMap<String,Object> newData){
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

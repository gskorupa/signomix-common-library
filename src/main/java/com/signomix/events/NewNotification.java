
package com.signomix.events;

import java.util.HashMap;
import org.cricketmsf.event.Event;

public class NewNotification extends Event {
    
    private HashMap<String,Object> data=null;

    public NewNotification() {
        super();
    }
    
    public NewNotification data(HashMap<String,Object> newData){
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

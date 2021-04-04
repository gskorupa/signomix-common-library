
package com.signomix.events;

import org.cricketmsf.event.Event;

public class NewDataEvent extends Event {
    
    private Object data=null; //must be IotData object

    public NewDataEvent() {
        super();
    }
    
    public NewDataEvent data(Object newData){
        this.data=newData;
        return this;
    }

    /**
     * @return the data
     */
    @Override
    public Object getData() {
        return data;
    }
    
}

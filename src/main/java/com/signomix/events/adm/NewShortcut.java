package com.signomix.events.adm;

import java.util.HashMap;
import org.cricketmsf.event.Event;

public class NewShortcut extends Event {

    private String data = null;

    public NewShortcut() {
        super();
    }
    
    public NewShortcut data(String data){
        this.data=data;
        return this;
    }

    /**
     * @return the data
     */
    @Override
    public String getData() {
        return data;
    }

}

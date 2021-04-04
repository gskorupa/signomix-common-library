package com.signomix.events;

import java.util.HashMap;
import org.cricketmsf.event.Event;

public class NewActuatorCommand extends Event {

    public static final int ACTUATOR_HEXCMD = 0;
    public static final int ACTUATOR_CMD = 1;

    private HashMap<String, Object> data = null;

    public NewActuatorCommand(){
        super();
        data = new HashMap<>();
    }
    public NewActuatorCommand(int type, String command, String origin) {
        super();
        data = new HashMap<>();
        data.put("type", Integer.valueOf(type));
        data.put("command", command);
        data.put("origin", origin);
    }
    
    public NewActuatorCommand data(HashMap<String, Object> data){
        this.data=data;
        return this;
    }

    /**
     * @return the data
     */
    @Override
    public HashMap<String, Object> getData() {
        return data;
    }

}

package com.signomix.events;

import org.cricketmsf.event.Procedures;
import org.cricketmsf.event.ProceduresIface;

/**
 *
 * @author greg
 */
public class SignomixProcedures extends Procedures implements ProceduresIface {
    public static final int CHECK_DEVICES = 1000;
    public static final int DO_BACKUP = 1001;
    
    public SignomixProcedures() { 
        super();
        add(CHECK_DEVICES, "CHECK_DEVICES");
        add(DO_BACKUP, "DO_BACKUP");
    }
}

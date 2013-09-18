package com.mangocity.util.log;

import org.apache.log4j.Level;

public class LoggerLevel extends Level {
    private static final long serialVersionUID = -7139953378177574879L;
    
    private final static int DB_LEVEL_INT = 60000;
    public final static Level DB = new LoggerLevel(DB_LEVEL_INT,"DB",7);
    
    protected LoggerLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }
}

package com.logwhatever.models;

import java.util.Date;
import java.util.UUID;

public class Tag extends EventData {
    public UUID UserId;
    public UUID EventId;
    public String Name;
    public UUID LogId;
    public String LogName;
    public Date Date;
    
    @Override
    public String toString() {
	return Name;
    }
}

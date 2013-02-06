package com.logwhatever.models;

import java.util.Date;
import java.util.UUID;

public class Measurement extends EventData {
    public UUID UserId;
    public UUID GroupId;
    public UUID LogId;
    public String LogName;
    public UUID EventId;
    public String Name;
    public float Quantity;
    public String Unit;
    public Date Date;
    
    @Override
    public String toString() {
	String result = "";
	if (Name != null && Name != "")
	    result = Name + ": ";
	result += Quantity;
	if (Unit != null && Unit != "")
	    result += " " + Unit;
	return result;
    }
}

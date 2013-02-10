package com.logwhatever.models;

import java.util.Date;
import java.util.UUID;

public class Measurement extends EventData {
    public UUID Id;
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
	if (Name != null && !Name.equals(""))
	    result = Name + ": ";
	result += Quantity;
	if (Unit != null && !Unit.equals(""))
	    result += " " + Unit;
	return result;
    }
}

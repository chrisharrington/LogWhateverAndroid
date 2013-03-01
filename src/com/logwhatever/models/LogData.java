package com.logwhatever.models;

import java.util.Date;
import java.util.List;

public class LogData {
    public User User;
    public String Name;
    public Date Date;
    public Date Time;
    public List<MeasurementData> Measurements;
    public List<Tag> Tags;
}

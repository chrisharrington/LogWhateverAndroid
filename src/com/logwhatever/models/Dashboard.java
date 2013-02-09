package com.logwhatever.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Dashboard {
    public UUID Id;
    public Date Date;
    public String Name;
    public List<Measurement> Measurements;
    public List<Tag> Tags;
}

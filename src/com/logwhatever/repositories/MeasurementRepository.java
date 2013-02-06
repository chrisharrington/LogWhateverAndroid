package com.logwhatever.repositories;

import com.logwhatever.models.Measurement;

public class MeasurementRepository extends BaseRepository<Measurement> implements IMeasurementRepository {
    public String getCollection() { return "measurements"; }
    public Class<Measurement> getType() { return Measurement.class; }
}

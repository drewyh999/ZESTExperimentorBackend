package com.zest.zestexperimentorbackend.persists.entities.Schedules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true, of = "stoppingCount")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public class EarlyStoppingSchedule extends Schedule{
    private final int stoppingCount;

    @JsonCreator
    public EarlyStoppingSchedule(int stoppingCount) {
        this.stoppingCount = stoppingCount;
    }
}

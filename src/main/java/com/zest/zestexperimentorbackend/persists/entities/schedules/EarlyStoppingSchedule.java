package com.zest.zestexperimentorbackend.persists.entities.schedules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@EqualsAndHashCode(callSuper = true, of = "stoppingCount")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@Getter
public class EarlyStoppingSchedule extends Schedule{
    protected final int stoppingCount;

    @JsonCreator
    public EarlyStoppingSchedule(int stoppingCount) {
        this.stoppingCount = stoppingCount;
    }
}

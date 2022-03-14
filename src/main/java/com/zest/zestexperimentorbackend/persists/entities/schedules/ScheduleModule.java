package com.zest.zestexperimentorbackend.persists.entities.schedules;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleModule {
    private List<String> questionIdList;

    public ScheduleModule(List<String> questionIdList) {
        this.questionIdList = questionIdList;
    }
}

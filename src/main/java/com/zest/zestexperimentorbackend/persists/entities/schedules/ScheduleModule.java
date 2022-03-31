package com.zest.zestexperimentorbackend.persists.entities.schedules;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleModule {
    public enum ModuleType{CODE,DEMO}

    private List<String> questionIdList;

    private ModuleType moduleType;

    public ScheduleModule(List<String> questionIdList) {
        this.questionIdList = questionIdList;
    }

}

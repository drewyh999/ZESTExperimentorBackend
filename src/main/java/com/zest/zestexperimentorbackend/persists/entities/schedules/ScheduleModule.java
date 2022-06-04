package com.zest.zestexperimentorbackend.persists.entities.schedules;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleModule {
    public enum ModuleType{CODE,DEMO}

    private List<String> questionIdList;

    private ModuleType moduleType;

    @JsonCreator
    public ScheduleModule(List<String> questionIdList) {
        this.questionIdList = questionIdList;
    }

}

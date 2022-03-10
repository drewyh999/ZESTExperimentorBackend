package com.zest.zestexperimentorbackend.persists.entities.Schedules;

import lombok.Data;

import java.util.List;


@Data
public class ScheduleModule {

    private String alias;

    private List<String> questionIdList;

}

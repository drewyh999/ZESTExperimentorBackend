package com.zest.zestexperimentorbackend.Entities.Schedules;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;


@Data
public class ScheduleModule {

    private String alias;

    private List<String> questionIdList;

}

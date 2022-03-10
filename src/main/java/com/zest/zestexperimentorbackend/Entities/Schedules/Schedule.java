package com.zest.zestexperimentorbackend.Entities.Schedules;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Schedules")
@Data
public class Schedule {
    @Id
    private String id;

    private String testGroup;

    private String alias;

    private List<ScheduleModule> scheduleModules;

}

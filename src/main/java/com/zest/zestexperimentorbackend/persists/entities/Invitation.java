package com.zest.zestexperimentorbackend.persists.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Invitations")
@Data
public class Invitation {
    @Id
    private String id;

    private String source;

    private Schedule.ScheduleType type;

    @JsonCreator
    public Invitation(String source, Schedule.ScheduleType type) {
        this.source = source;
        this.type = type;
    }
}

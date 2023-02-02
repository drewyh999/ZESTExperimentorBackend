package com.zest.zestexperimentorbackend.persists.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import lombok.Data;

@Data
public class InvitationDTO {
    private String source;

    private Schedule.ScheduleType type;

    @JsonCreator
    public InvitationDTO(String source, Schedule.ScheduleType type) {
        this.source = source;
        this.type = type;
    }
}

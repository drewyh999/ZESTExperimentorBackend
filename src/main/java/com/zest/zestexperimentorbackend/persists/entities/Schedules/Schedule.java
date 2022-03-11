package com.zest.zestexperimentorbackend.persists.entities.Schedules;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Schedules")
@Data
@JsonSubTypes({
        @JsonSubTypes.Type(value = EarlyStoppingSchedule.class,name = "EarlyStoppingSchedule")
}
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public class Schedule {
    @Id
    private String id;

    private String testGroup;

    private String alias;

    private List<String> questionIdList;

}

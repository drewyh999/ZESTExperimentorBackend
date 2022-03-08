package com.zest.zestexperimentorbackend.Entities.Schedules;

import com.zest.zestexperimentorbackend.Entities.Questions.BaseQuestion;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Schedules")
@Data
public class BaseSchedule {
    @Id
    private String id;

    private String testGroup;

    private String alias;

    private List<String> questionIdList;

}

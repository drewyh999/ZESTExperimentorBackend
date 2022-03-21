package com.zest.zestexperimentorbackend.persists.entities.questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zest.zestexperimentorbackend.persists.entities.questionchoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.questionmedias.QuestionMedia;
import lombok.*;

@EqualsAndHashCode(callSuper = true,of = "exposureTime")
@Data
@JsonTypeName("TimedQuestion")
public class TimedQuestion extends BaseQuestion{
    protected long exposureTime;
}

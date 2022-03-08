package com.zest.zestexperimentorbackend.Entities.QuestionChoices;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zest.zestexperimentorbackend.Entities.Questions.DemographicQuestion;
import com.zest.zestexperimentorbackend.Entities.Questions.TimedQuestion;
import org.springframework.data.annotation.TypeAlias;

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = MultipleTextChoice.class,name = "MultipleTextChoice"),
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface QuestionChoice {
}

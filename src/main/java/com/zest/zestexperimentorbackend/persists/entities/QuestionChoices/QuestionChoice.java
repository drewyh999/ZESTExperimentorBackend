package com.zest.zestexperimentorbackend.persists.entities.QuestionChoices;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = MultipleTextChoice.class,name = "MultipleTextChoice"),
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface QuestionChoice {
}

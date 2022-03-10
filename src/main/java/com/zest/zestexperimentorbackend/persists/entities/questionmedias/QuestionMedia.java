package com.zest.zestexperimentorbackend.persists.entities.questionmedias;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = TextMedia.class,name = "TextMedia"),
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface QuestionMedia {

}

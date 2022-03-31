package com.zest.zestexperimentorbackend.persists.entities.questions;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection="Questions")
@Data
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DemographicQuestion.class),
                @JsonSubTypes.Type(value = CodeEvaluation.class)
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
/*
* Make sure to add subtype info above if wish to extend more questions, and make sure to add @Data annotation to the
* extended subclass so that jackson could serialize into subtypes properly*/
public abstract class BaseQuestion {
    public enum QuestionType{MULTI_CHOICE,SINGLE_CHOICE,TEXT}
    @Id
    protected String id;

    protected String questionText;

    protected QuestionType questionType;

    protected List<String> questionChoices;

    protected String alias;


}

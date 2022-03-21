package com.zest.zestexperimentorbackend.persists.entities.questions;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zest.zestexperimentorbackend.persists.entities.questionchoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.questionmedias.QuestionMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection="Questions")
@Data
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PlainQuestion.class),
                @JsonSubTypes.Type(value = TimedQuestion.class)
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

    protected QuestionMedia questionMedia;

    protected QuestionType questionType;

    protected QuestionChoice choice;

    protected String alias;


}

package com.zest.zestexperimentorbackend.persists.entities.Questions;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zest.zestexperimentorbackend.persists.entities.QuestionChoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.QuestionMedias.QuestionMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection="Questions")
@Data
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DemographicQuestion.class,name = "DemographicQuestion"),
                @JsonSubTypes.Type(value = TimedQuestion.class, name = "TimedQuestion")
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public class BaseQuestion {
    public enum QuestionType{MULTI_CHOICE,SINGLE_CHOICE,TEXT}
    @Id
    @EqualsAndHashCode.Exclude protected String id;

    protected QuestionMedia questionMedia;

    protected QuestionType questionType;

    protected QuestionChoice choice;

    protected String alias;

    public BaseQuestion(QuestionMedia questionMedia, QuestionType questionType, QuestionChoice choice, String alias) {
        this.questionMedia = questionMedia;
        this.questionType = questionType;
        this.choice = choice;
        this.alias = alias;
    }
}

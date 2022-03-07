package com.zest.zestexperimentorbackend.Entities.Questions;


import com.zest.zestexperimentorbackend.Entities.QuestionChoices.QuestionChoice;
import com.zest.zestexperimentorbackend.Entities.QuestionMedias.QuestionMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection="Questions")
@Data
public abstract class BaseQuestion {
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

    public BaseQuestion(){

    }
}

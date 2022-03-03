package com.zest.zestexperimentorbackend.Entities.Questions;


import com.zest.zestexperimentorbackend.Entities.QuestionChoices.BaseQuestionChoice;
import com.zest.zestexperimentorbackend.Entities.QuestionMedias.BaseQuestionMedia;
import org.springframework.data.annotation.Id;
enum QuestionType{MULTI_CHOICE,SINGLE_CHOICE,}

public abstract class BaseQuestion {
    @Id
    protected String id;

    protected BaseQuestionMedia questionMedia;

    protected QuestionType questionType;

    protected BaseQuestionChoice choice;
}

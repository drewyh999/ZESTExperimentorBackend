package com.zest.zestexperimentorbackend.Entities.Questions;

import com.zest.zestexperimentorbackend.Entities.QuestionChoices.QuestionChoice;
import com.zest.zestexperimentorbackend.Entities.QuestionMedias.QuestionMedia;


public class DemographicQuestion extends BaseQuestion{
    public DemographicQuestion(QuestionMedia questionMedia, QuestionType questionType, QuestionChoice choice, String alias) {
        super(questionMedia, questionType, choice, alias);
    }
}

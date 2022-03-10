package com.zest.zestexperimentorbackend.persists.entities.Questions;

import com.zest.zestexperimentorbackend.persists.entities.questionchoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.questionmedias.QuestionMedia;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TypeAlias("DemoQuest")
public class DemographicQuestion extends BaseQuestion{
    public DemographicQuestion(QuestionMedia questionMedia, QuestionType questionType, QuestionChoice choice, String alias) {
        super(questionMedia, questionType, choice, alias);
    }
}

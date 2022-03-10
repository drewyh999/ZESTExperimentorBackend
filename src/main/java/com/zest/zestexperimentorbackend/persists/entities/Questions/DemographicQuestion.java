package com.zest.zestexperimentorbackend.persists.entities.Questions;

import com.zest.zestexperimentorbackend.persists.entities.QuestionChoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.QuestionMedias.QuestionMedia;
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

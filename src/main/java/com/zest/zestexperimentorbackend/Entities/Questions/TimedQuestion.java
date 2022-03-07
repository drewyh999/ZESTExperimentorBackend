package com.zest.zestexperimentorbackend.Entities.Questions;

import com.zest.zestexperimentorbackend.Entities.QuestionChoices.QuestionChoice;
import com.zest.zestexperimentorbackend.Entities.QuestionMedias.QuestionMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true,of = "exposureTime")
@Data
public class TimedQuestion extends BaseQuestion{
    private long exposureTime;

    public TimedQuestion(QuestionMedia questionMedia, QuestionType questionType, QuestionChoice choice, String alias, long exposureTime) {
        super(questionMedia, questionType, choice, alias);
        this.exposureTime = exposureTime;
    }
}

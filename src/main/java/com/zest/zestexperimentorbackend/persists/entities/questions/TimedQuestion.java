package com.zest.zestexperimentorbackend.persists.entities.questions;

import com.zest.zestexperimentorbackend.persists.entities.questionchoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.questionmedias.QuestionMedia;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;


@EqualsAndHashCode(callSuper = true,of = "exposureTime")
@TypeAlias("TimeQuest")
@ToString(callSuper = true)
public class TimedQuestion extends BaseQuestion{
    private final long exposureTime;

    public TimedQuestion(QuestionMedia questionMedia, QuestionType questionType, QuestionChoice choice, String alias, long exposureTime) {
        super(questionMedia, questionType, choice, alias);
        this.exposureTime = exposureTime;
    }
}

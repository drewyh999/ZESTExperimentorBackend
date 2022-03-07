package com.zest.zestexperimentorbackend.Entities.QuestionMedias;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "questionText")
@ToString
public class TextQuestionMedia implements QuestionMedia {
    private final String questionText;

    public TextQuestionMedia(String questionText) {
        this.questionText = questionText;
    }

}

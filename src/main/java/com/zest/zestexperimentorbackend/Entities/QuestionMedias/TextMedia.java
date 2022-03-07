package com.zest.zestexperimentorbackend.Entities.QuestionMedias;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "questionText")
@ToString
public class TextMedia implements QuestionMedia {
    public final String questionText;

    public TextMedia(String questionText) {
        this.questionText = questionText;
    }

}

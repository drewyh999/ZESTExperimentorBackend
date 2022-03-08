package com.zest.zestexperimentorbackend.Entities.QuestionMedias;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "questionText")
@ToString
public class TextMedia implements QuestionMedia {
    public final String questionText;

    @JsonCreator
    public TextMedia(String questionText) {
        this.questionText = questionText;
    }

}

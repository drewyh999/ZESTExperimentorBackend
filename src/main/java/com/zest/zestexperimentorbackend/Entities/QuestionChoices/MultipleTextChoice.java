package com.zest.zestexperimentorbackend.Entities.QuestionChoices;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MultipleTextChoice implements QuestionChoice {
    private final List<String> choices;

    public MultipleTextChoice(List<String> choices) {
        this.choices = choices;
    }
}

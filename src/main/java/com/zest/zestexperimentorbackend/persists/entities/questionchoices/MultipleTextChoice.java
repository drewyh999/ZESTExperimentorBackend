package com.zest.zestexperimentorbackend.persists.entities.questionchoices;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(of = "choices")
public class MultipleTextChoice implements QuestionChoice {


    private final List<String> choices;

    @JsonCreator
    public MultipleTextChoice(List<String> choices) {
        this.choices = choices;
    }
}

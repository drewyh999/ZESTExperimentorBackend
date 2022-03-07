package com.zest.zestexperimentorbackend.Entities.QuestionChoices;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(of = "choices")
public class MultipleTextChoice implements QuestionChoice {
    private final List<String> choices;

    public MultipleTextChoice(List<String> choices) {
        this.choices = choices;
    }
}

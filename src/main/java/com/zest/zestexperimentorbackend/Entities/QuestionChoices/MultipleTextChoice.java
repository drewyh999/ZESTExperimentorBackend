package com.zest.zestexperimentorbackend.Entities.QuestionChoices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public MultipleTextChoice(List<String> choices) {
        this.choices = choices;
    }
}

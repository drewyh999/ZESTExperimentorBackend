package com.zest.zestexperimentorbackend.persists.entities.answers;

import lombok.Data;

@Data
public class Answer {
    String questionID;

    String answerText;

    Long timeSpent;

    public Answer(String questionID) {
        this.questionID = questionID;
        this.answerText = null;
        this.timeSpent = null;
    }

    public Answer(String questionID, String answerText, Long timeSpent) {
        this.questionID = questionID;
        this.answerText = answerText;
        this.timeSpent = timeSpent;
    }

    public Answer(){

    }
}

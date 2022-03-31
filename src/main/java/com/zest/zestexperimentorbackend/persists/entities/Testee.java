package com.zest.zestexperimentorbackend.persists.entities;

import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "Testees")
@Data
public class Testee {
    @Id
    private String id;

    private List<Answer> answerList;

    private String testGroup;

    private boolean finished;

    public Testee(String testGroup) {
        this.testGroup = testGroup;
        this.answerList = new ArrayList<>();
        this.finished = false;
    }

}


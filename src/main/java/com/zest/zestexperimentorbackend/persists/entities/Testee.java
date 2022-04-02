package com.zest.zestexperimentorbackend.persists.entities;

import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "Testees")
@Data
public class Testee {
    @Id
    private String id;

    private Map<String, Answer> answerMap;

    private String testGroup;

    private boolean finished;

    public Testee(String testGroup) {
        this.testGroup = testGroup;
        this.answerMap = new HashMap<>();
        this.finished = false;
    }

}


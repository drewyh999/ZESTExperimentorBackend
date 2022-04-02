package com.zest.zestexperimentorbackend.persists.entities.cacheobjects;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RedisHash("AnswerStateCache")
@Data
public class AnswerStateCache {
    String id;

    int questionIndex;

    int moduleIndex;

    String testeeId;

    String scheduleId;

    List<String> currentModuleQuestionIDList;

    public AnswerStateCache(String id, int questionIndex, int moduleIndex, String testeeId, String scheduleId, List<String> currentModuleQuestionIDList) {
        this.id = id;
        this.questionIndex = questionIndex;
        this.moduleIndex = moduleIndex;
        this.testeeId = testeeId;
        this.scheduleId = scheduleId;
        this.currentModuleQuestionIDList = currentModuleQuestionIDList;
    }

    public AnswerStateCache(){}
}

package com.zest.zestexperimentorbackend.persists.entities.cacheobjects;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;

@RedisHash("AnswerStateCache")
@Data
public class AnswerStateCache {
    String id;

    int questionIndex;

    int moduleIndex;

    String testeeId;

    String scheduleId;

    public AnswerStateCache(String id, int questionIndex, int moduleIndex, String testeeId, String scheduleId) {
        this.id = id;
        this.questionIndex = questionIndex;
        this.moduleIndex = moduleIndex;
        this.testeeId = testeeId;
        this.scheduleId = scheduleId;
    }
}

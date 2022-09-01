package com.zest.zestexperimentorbackend.cache;

import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@RedisHash("AnswerStateCache")
@Data
public class AnswerStateCache {
    @Indexed
    String id;

    int questionIndex;

    int moduleIndex;

    String testeeId;

    @Indexed
    String scheduleId;

    @ToString.Exclude List<BaseQuestion> currentModuleQuestionList;

    public AnswerStateCache(String id, int questionIndex, int moduleIndex, String testeeId, String scheduleId, List<BaseQuestion> currentModuleQuestionList) {
        this.id = id;
        this.questionIndex = questionIndex;
        this.moduleIndex = moduleIndex;
        this.testeeId = testeeId;
        this.scheduleId = scheduleId;
        this.currentModuleQuestionList = currentModuleQuestionList;
    }

    public AnswerStateCache(){}
}

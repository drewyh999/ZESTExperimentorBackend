package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends BaseRepository<BaseQuestion> {
    List<BaseQuestion> findAllByQuestionTypeIs(BaseQuestion.QuestionType type);
    List<BaseQuestion> findAllByAliasContains(String alias);
}

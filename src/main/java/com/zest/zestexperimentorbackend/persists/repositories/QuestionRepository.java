package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<BaseQuestion,String> {
    BaseQuestion findByAliasIs(String alias);

    List<BaseQuestion> findAllByAliasContains(String alias);

    List<BaseQuestion> findAllByChoiceExists();
}

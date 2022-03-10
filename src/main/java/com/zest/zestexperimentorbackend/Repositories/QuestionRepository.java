package com.zest.zestexperimentorbackend.Repositories;

import com.zest.zestexperimentorbackend.Entities.QuestionMedias.QuestionMedia;
import com.zest.zestexperimentorbackend.Entities.Questions.BaseQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<BaseQuestion,String> {
    BaseQuestion findByAliasIs(String alias);

    List<BaseQuestion> findAllByAliasContains(String alias);

    List<BaseQuestion> findAllByChoiceExists();
}

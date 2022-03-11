package com.zest.zestexperimentorbackend.services;


import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import com.zest.zestexperimentorbackend.persists.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService extends BaseCrudService<BaseQuestion> {

    public QuestionService(@Qualifier("questionRepository") BaseRepository<BaseQuestion> repository) {
        super(repository);
    }

    public List<BaseQuestion> getQuestionByType(BaseQuestion.QuestionType type){
        return ((QuestionRepository)repository).findAllByQuestionTypeIs(type);
    }

    public List<BaseQuestion> getByAlias(String alias){
        return ((QuestionRepository)repository).findAllByAliasContains(alias);
    }
}

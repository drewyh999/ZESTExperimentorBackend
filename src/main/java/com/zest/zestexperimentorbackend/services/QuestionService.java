package com.zest.zestexperimentorbackend.services;


import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import com.zest.zestexperimentorbackend.persists.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService extends BaseCrudService<BaseQuestion> {

    public QuestionService(@Qualifier("questionRepository") BaseRepository<BaseQuestion> repository) {
        super(repository);
    }

    public List<BaseQuestion> getQuestionByChoiceType(BaseQuestion.QuestionChoiceType type){
        return ((QuestionRepository)repository).findAllByQuestionChoiceTypeIs(type);
    }

    public List<BaseQuestion> getByAlias(String alias){
        return ((QuestionRepository)repository).findAllByAliasContains(alias);
    }

    public List<BaseQuestion> getByIdList(List<String> keySet) throws BaseNotFoundExeption{
        List<BaseQuestion> result = new ArrayList<>();
        for(String key : keySet){
            result.add((repository).findById(key).orElseThrow(() -> new BaseNotFoundExeption(key,"Basequesion")));
        }
        return result;
    }
}

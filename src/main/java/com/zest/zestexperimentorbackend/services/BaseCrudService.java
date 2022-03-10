package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.exceptions.QuestionNotFoundException;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

public class BaseCrudService<T>{
    public final BaseRepository<T> repository;

    public BaseCrudService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    public void save(List<T> questionList){
        repository.saveAll(questionList);
    }

    public T findById(String id){
        return repository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }

    public void deleteById(String id){
        repository.deleteById(id);
    }

    public List<T> getByAlias(String alias){
        if(alias.equals("")){
            return repository.findAll();
        }
        else{
            return repository.findAllByAliasContains(alias);
        }
    }
}

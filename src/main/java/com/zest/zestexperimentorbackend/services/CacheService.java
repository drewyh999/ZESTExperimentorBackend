package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.persists.entities.cacheobjects.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.repositories.CacheRepository;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    public final CacheRepository repository;

    public CacheService(CacheRepository cacheRepository) {
        this.repository = cacheRepository;
    }

    public AnswerStateCache saveOne(AnswerStateCache item){
        return repository.save(item);
    }

    public AnswerStateCache getById(String id){
        return repository.findById(id).orElseThrow(() -> new BaseNotFoundExeption(id,repository.getClass().toString()));
    }

    public void deleteById(String id){
        repository.deleteById(id);
    }
}

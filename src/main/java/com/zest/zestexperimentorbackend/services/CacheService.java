package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.cache.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.repositories.CacheRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CacheService {
    public final CacheRepository repository;

    public CacheService(CacheRepository cacheRepository) {
        this.repository = cacheRepository;
    }

    public void saveOne(AnswerStateCache item){
        repository.save(item);
    }

    public AnswerStateCache getById(String id){
        return repository.findById(id).orElseThrow(() -> new BaseNotFoundExeption(id,repository.getClass().toString()));
    }

    public void deleteById(String id){
        repository.deleteById(id);
    }

    public List<String> getTesteeIdsInCacheByScheduleId(String scheduleId){
        var caches =  repository.findAllByScheduleIdIs(scheduleId);
        return caches.stream().map(AnswerStateCache::getTesteeId).collect(Collectors.toList());
    }
}

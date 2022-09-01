package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.cache.AnswerStateCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CacheRepository extends CrudRepository<AnswerStateCache,String> {
    List<AnswerStateCache> findAllByScheduleIdIs(String scheduleId);
}

package com.zest.zestexperimentorbackend.Repositories;

import com.zest.zestexperimentorbackend.Entities.Schedules.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    Schedule findByAliasIs(String alias);

    List<Schedule> findAllByAliasContains(String alias);
}

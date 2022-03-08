package com.zest.zestexperimentorbackend.Repositories;

import com.zest.zestexperimentorbackend.Entities.Schedules.BaseSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<BaseSchedule, String> {
}

package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.persists.entities.Schedules.Schedule;

import java.util.List;

public interface ScheduleRepository extends BaseRepository<Schedule> {
    List<Schedule> findAllByAliasContains(String alias);
}

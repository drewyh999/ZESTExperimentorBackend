package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;

import java.util.List;

public interface ScheduleRepository extends BaseRepository<Schedule> {
    List<Schedule> findAllByAliasContains(String alias);
    List<Schedule> findAllByScheduleTypeIs(Schedule.ScheduleType scheduleType);
}

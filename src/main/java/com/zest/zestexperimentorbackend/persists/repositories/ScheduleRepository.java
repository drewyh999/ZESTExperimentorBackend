package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ScheduleRepository extends BaseRepository<Schedule> {
    List<Schedule> findAllByAliasContains(String alias);
    List<Schedule> findAllByScheduleTypeIs(Schedule.ScheduleType scheduleType);

    @Query(value = "{'scheduleModuleList.questionIdList': { $all: [?0] }}", fields = "{'id' :  1}")
    List<String> findAllByScheduleModuleListQuestionIdListContains(String questionId);
}

package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import com.zest.zestexperimentorbackend.persists.repositories.ScheduleRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScheduleService extends BaseCrudService<Schedule>{
    private static final Log log = LogFactory.getLog(ScheduleService.class);

    public ScheduleService(@Qualifier("scheduleRepository") BaseRepository<Schedule> repository) {
        super(repository);
    }
    public List<Schedule> getByAlias(String alias){
        return ((ScheduleRepository)repository).findAllByAliasContains(alias);
    }
    public List<Schedule> getByType(Schedule.ScheduleType type){
        return ((ScheduleRepository)repository).findAllByScheduleTypeIs(type);
    }


    public List<String> getScheduleIdsByQuestionId(String questionId) {
        log.info("Incoming query for schedules containing " + questionId);
        var result =   ((ScheduleRepository)repository).findAllByScheduleModuleListQuestionIdListContains(questionId);
        log.info(result);
        return result;
    }
}
package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.Schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import com.zest.zestexperimentorbackend.persists.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService extends BaseCrudService<Schedule>{
    public ScheduleService(@Qualifier("scheduleRepository") BaseRepository<Schedule> repository) {
        super(repository);
    }
}

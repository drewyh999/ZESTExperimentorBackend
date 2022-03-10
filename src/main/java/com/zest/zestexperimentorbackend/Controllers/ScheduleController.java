package com.zest.zestexperimentorbackend.Controllers;


import com.zest.zestexperimentorbackend.Entities.Schedules.Schedule;
import com.zest.zestexperimentorbackend.Exceptions.QuestionNotFoundException;
import com.zest.zestexperimentorbackend.Exceptions.ScheduleNotFoundException;
import com.zest.zestexperimentorbackend.Repositories.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/schedules")
    List<Schedule> allSchedules(@RequestParam(value="alias",defaultValue = "") String alias){
        if(alias.equals(""))
            return scheduleRepository.findAll();
        else
            return scheduleRepository.findAllByAliasContains(alias);
    }

    @PostMapping("/schedules")
    @ResponseStatus(HttpStatus.OK)
    void updatedSchedules(@RequestBody List<Schedule> scheduleList){
        scheduleRepository.saveAll(scheduleList);
    }

    @GetMapping("/schedules/{id}")
    Schedule getSchedule(@PathVariable String id){
        return scheduleRepository.findById(id).orElseThrow(() -> new ScheduleNotFoundException(id));
    }

    @DeleteMapping("/schedule/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deletedSchedule(@PathVariable String id){
        scheduleRepository.deleteById(id);
    }

}
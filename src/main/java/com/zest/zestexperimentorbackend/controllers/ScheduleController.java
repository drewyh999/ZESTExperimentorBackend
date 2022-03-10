package com.zest.zestexperimentorbackend.controllers;


import com.zest.zestexperimentorbackend.persists.entities.Schedules.Schedule;
import com.zest.zestexperimentorbackend.exceptions.ScheduleNotFoundException;

import com.zest.zestexperimentorbackend.services.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedules")
    List<Schedule> allSchedules(@RequestParam(value="alias",defaultValue = "") String alias){
        return scheduleService.getByAlias(alias);
    }

    @PostMapping("/schedules")
    @ResponseStatus(HttpStatus.OK)
    void updatedSchedules(@RequestBody List<Schedule> scheduleList){
        scheduleService.save(scheduleList);
    }

    @GetMapping("/schedules/{id}")
    Schedule getSchedule(@PathVariable String id){
        return scheduleService.findById(id);
    }

    @DeleteMapping("/schedule/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deletedSchedule(@PathVariable String id){
        scheduleService.deleteById(id);
    }

}

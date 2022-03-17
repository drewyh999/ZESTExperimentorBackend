package com.zest.zestexperimentorbackend.controllers;


import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;

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

    @GetMapping(value = "/schedules",params = "alias")
    List<Schedule> getSchedulesByAlias(String alias){
        return scheduleService.getByAlias(alias);
    }

    @GetMapping("/schedules")
    List<Schedule> allSchedules(){
        return scheduleService.getAll();
    }

    @PostMapping("/schedules")
    @ResponseStatus(HttpStatus.OK)
    void updatedSchedules(@RequestBody List<Schedule> scheduleList){
        scheduleService.save(scheduleList);
    }

    @GetMapping("/schedules/{id}")
    Schedule getSchedule(@PathVariable String id){
        return scheduleService.getById(id);
    }

    @DeleteMapping("/schedule/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deletedSchedule(@PathVariable String id){
        scheduleService.deleteById(id);
    }

}

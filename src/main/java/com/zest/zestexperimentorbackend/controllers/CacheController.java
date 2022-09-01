package com.zest.zestexperimentorbackend.controllers;


import com.zest.zestexperimentorbackend.services.CacheService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CacheController {
    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    // Used to test if the schedule is related to a cache object in redis so that the deletion won't cause 404
    @GetMapping("/cache/contains_schedule/{scheduleId}")
    List<String> getTesteeIdsInCacheByScheduleId(@PathVariable String scheduleId){
        return cacheService.getTesteeIdsInCacheByScheduleId(scheduleId);
    }
}

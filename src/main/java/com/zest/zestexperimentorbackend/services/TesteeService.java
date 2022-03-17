package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import com.zest.zestexperimentorbackend.persists.repositories.TesteeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TesteeService extends BaseCrudService<Testee>{
    public TesteeService(@Qualifier("testeeRepository") BaseRepository<Testee> repository) {
        super(repository);
    }

    public List<Testee> findByTestGroupContains(String testgroupString){
        return ((TesteeRepository)repository).findAllByTestGroupContains(testgroupString);
    }
}

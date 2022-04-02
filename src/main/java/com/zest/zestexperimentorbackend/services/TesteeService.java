package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import com.zest.zestexperimentorbackend.persists.repositories.TesteeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;



@Service
public class TesteeService extends BaseCrudService<Testee>{
    public TesteeService(@Qualifier("testeeRepository") BaseRepository<Testee> repository) {
        super(repository);
    }

    public Stream<Testee> findByTestGroupContains(String testgroupString){
        return ((TesteeRepository)repository).findAllByTestGroupContains(testgroupString);
    }

    public long findAmountByIsFinishing(Boolean finished){
        if(finished == null){
            return ((TesteeRepository)repository).findAllBy().count();
        }
        return ((TesteeRepository)repository).findAllByFinishedIs(finished).count();
    }
}

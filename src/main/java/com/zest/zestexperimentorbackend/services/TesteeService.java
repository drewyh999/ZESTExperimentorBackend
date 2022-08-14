package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;
import com.zest.zestexperimentorbackend.persists.repositories.TesteeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.stream.Stream;



@Service
public class TesteeService extends BaseCrudService<Testee>{
    public TesteeService(@Qualifier("testeeRepository") BaseRepository<Testee> repository) {
        super(repository);
    }

    public Stream<Testee> getByTestGroupContains(String testgroupString){
        return ((TesteeRepository)repository).findAllByTestGroupContains(testgroupString);
    }

    public long getAmountByIsFinishingAndMode(Boolean finished, String mode){
        if(finished == null){
            return ((TesteeRepository)repository).findAllBy().filter(t -> t.getTestGroup().contains(mode)).count();
        }
        return ((TesteeRepository)repository).findAllByFinishedIs(finished).filter(t -> t.getTestGroup().contains(mode)).count();
    }

    public boolean ifAnyParticipants(String mode){
        return this.getByTestGroupContains(mode).findAny().isPresent();
    }
}

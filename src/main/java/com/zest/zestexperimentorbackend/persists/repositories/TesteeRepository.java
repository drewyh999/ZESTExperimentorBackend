package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TesteeRepository extends BaseRepository<Testee> {
    List<Testee> findAllByTestGroupContains(String testGroup);
}

package com.zest.zestexperimentorbackend.Repositories;

import com.zest.zestexperimentorbackend.Entities.Testee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TesteeRepository extends MongoRepository<Testee,String> {
}

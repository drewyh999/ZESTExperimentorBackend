package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TesteeRepository extends MongoRepository<Testee,String> {
}

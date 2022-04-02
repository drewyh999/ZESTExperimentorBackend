package com.zest.zestexperimentorbackend.persists.repositories;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.stream.Stream;

public interface TesteeRepository extends BaseRepository<Testee> {
    Stream<Testee> findAllByTestGroupContains(String testGroup);

    /**Use Stream in case we have millions of testee records in the database
     *
     * @return the stream consisting of
     */
    Stream<Testee> findAllBy();

    Stream<Testee> findAllByFinishedIs(Boolean finished);
}

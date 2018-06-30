package com.yoga.core.sequence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepository extends CrudRepository<Sequence, Long> {

    @Query("select max(id) from Sequence")
    public Long maxId();
}


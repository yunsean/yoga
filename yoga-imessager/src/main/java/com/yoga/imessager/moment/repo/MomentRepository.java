package com.yoga.imessager.moment.repo;

import com.yoga.imessager.group.model.Group;
import com.yoga.imessager.moment.model.Moment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {


}

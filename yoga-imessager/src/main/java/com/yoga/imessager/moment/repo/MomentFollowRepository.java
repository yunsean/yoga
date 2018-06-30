package com.yoga.imessager.moment.repo;

import com.yoga.imessager.moment.model.Moment;
import com.yoga.imessager.moment.model.MomentFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MomentFollowRepository extends JpaRepository<MomentFollow, Long> {

    
}

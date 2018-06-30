package com.yoga.imessager.moment.repo;

import com.yoga.imessager.moment.model.Moment;
import com.yoga.imessager.moment.model.MomentUpvote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MomentUpvoteRepository extends JpaRepository<MomentUpvote, Long> {


}

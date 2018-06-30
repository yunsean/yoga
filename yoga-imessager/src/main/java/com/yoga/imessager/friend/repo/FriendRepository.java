package com.yoga.imessager.friend.repo;

import com.yoga.core.repository.BaseRepository;
import com.yoga.imessager.friend.model.Friend;
import com.yoga.imessager.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "imFriendRepository")
public interface FriendRepository extends BaseRepository<Friend, Long> {

    List<Friend> findByUserIdAndAllowMoment(long userId, boolean allowMoment);
    Friend findByUserIdAndFriendId(long userId, long friendId);
}

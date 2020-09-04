package com.yoga.content.comment.mapper;

import com.yoga.content.comment.ao.FavoriteCount;
import com.yoga.content.comment.ao.UpvoteCount;
import com.yoga.content.comment.model.Favorite;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FavoriteMapper extends MyMapper<Favorite> {
    List<FavoriteCount> latestFavoriteCount(@Param("afterTime") LocalDateTime afterTime);
}

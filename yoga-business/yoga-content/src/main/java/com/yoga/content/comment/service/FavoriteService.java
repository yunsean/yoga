package com.yoga.content.comment.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.content.article.service.ArticleService;
import com.yoga.content.comment.ao.CommentCount;
import com.yoga.content.comment.ao.FavoriteCount;
import com.yoga.content.comment.mapper.FavoriteMapper;
import com.yoga.content.comment.model.Favorite;
import com.yoga.core.base.BaseService;
import com.yoga.core.mybatis.MapperQuery;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("cmsFavoriteService")
public class FavoriteService extends BaseService {

    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private ArticleService articleService;

    public void add(long tenantId, long favoriterId, String articleId) {
        if (new MapperQuery<>(Favorite.class)
                .andEqualTo("favoriterId", favoriterId)
                .andEqualTo("articleId", articleId)
                .count(favoriteMapper) > 0) return;
        Favorite favorite = new Favorite(tenantId, articleId, favoriterId);
        favoriteMapper.insert(favorite);
    }
    public void delete(long tenantId, long favoriterId, String articleId) {
        new MapperQuery<>(Favorite.class)
                .andEqualTo("favoriterId", favoriterId)
                .andEqualTo("articleId", articleId)
                .delete(favoriteMapper);
    }
    public boolean isFavorite(long tenantId, long favoriterId, String articleId) {
        return new MapperQuery<>(Favorite.class)
                .andEqualTo("favoriterId", favoriterId)
                .andEqualTo("articleId", articleId)
                .count(favoriteMapper) > 0;
    }
    public PageInfo<Favorite> list(long tenantId, long favoriterId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Favorite> articleFavorites = new MapperQuery<>(Favorite.class)
                .andEqualTo("favoriterId", favoriterId)
                .query(favoriteMapper);
        return new PageInfo<>(articleFavorites);
    }
    public PageInfo<Document> list(long tenantId, long favoriterId, String[] fields, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Favorite> favorites = new MapperQuery<>(Favorite.class)
                .andEqualTo("favoriterId", favoriterId)
                .query(favoriteMapper);
        PageInfo<Favorite> pageInfo = new PageInfo<>(favorites);
        PageInfo<Document> result = new PageInfo<>();
        result.setTotal(pageInfo.getTotal());
        result.setPageSize(pageInfo.getPageSize());
        result.setPageNum(pageInfo.getPageNum());
        result.setPages(pageInfo.getPages());
        if (pageInfo.getList() == null || pageInfo.getList().size() < 1) return result;
        List<String> ids = favorites.stream().map(Favorite::getArticleId).collect(Collectors.toList());
        List<Document> documents = articleService.get(tenantId, ids, fields);
        result.setList(documents);
        return result;
    }
}

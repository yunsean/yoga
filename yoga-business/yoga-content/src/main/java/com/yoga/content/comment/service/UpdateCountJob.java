package com.yoga.content.comment.service;

import com.yoga.content.article.ao.FocusCount;
import com.yoga.content.article.service.ArticleService;
import com.yoga.content.comment.ao.CommentCount;
import com.yoga.content.comment.ao.FavoriteCount;
import com.yoga.content.comment.ao.UpvoteCount;
import com.yoga.content.comment.mapper.CommentMapper;
import com.yoga.content.comment.mapper.FavoriteMapper;
import com.yoga.content.comment.mapper.UpvoteMapper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@DisallowConcurrentExecution
public class UpdateCountJob implements Job {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UpvoteMapper upvoteMapper;
    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private ArticleService articleService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            LocalDateTime afterTime = LocalDateTime.now().minusHours(1);
            List<CommentCount> commentCounts = commentMapper.latestCommentCount(afterTime);
            List<UpvoteCount> upvoteCounts = upvoteMapper.latestUpvoteCount(afterTime);
            List<FavoriteCount> favoriteCounts = favoriteMapper.latestFavoriteCount(afterTime);

            Map<String, FocusCount> focusCounts = commentCounts.stream().map(it -> new FocusCount(it.getTenantId(), it.getArticleId(), it.getCommentCount())).collect(Collectors.toMap(FocusCount::getArticleId, it -> it));
            for (UpvoteCount count : upvoteCounts) {
                if (focusCounts.containsKey(count.getArticleId()))
                    focusCounts.get(count.getArticleId()).setUpvoteCount(count.getUpvoteCount());
                else
                    focusCounts.put(count.getArticleId(), new FocusCount(count.getTenantId(), count.getArticleId(), null, count.getUpvoteCount(), null));
            }
            for (FavoriteCount count : favoriteCounts) {
                if (focusCounts.containsKey(count.getArticleId()))
                    focusCounts.get(count.getArticleId()).setFavoriteCount(count.getFavoriteCount());
                else
                    focusCounts.put(count.getArticleId(), new FocusCount(count.getTenantId(), count.getArticleId(), null, null, count.getFavoriteCount()));
            }
            if (focusCounts.size() > 0) {
                articleService.updateFocusCount(focusCounts.values());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

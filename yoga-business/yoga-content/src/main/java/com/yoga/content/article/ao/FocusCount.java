package com.yoga.content.article.ao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FocusCount {
    private Long tenantId;
    private String articleId;
    private Integer commentCount;
    private Integer upvoteCount;
    private Integer favoriteCount;

    public FocusCount(Long tenantId, String articleId, Integer commentCount) {
        this.tenantId = tenantId;
        this.articleId = articleId;
        this.commentCount = commentCount;
    }
    public FocusCount(Long tenantId, String articleId, Integer commentCount, Integer upvoteCount, Integer favoriteCount) {
        this.tenantId = tenantId;
        this.articleId = articleId;
        this.commentCount = commentCount;
        this.upvoteCount = upvoteCount;
        this.favoriteCount = favoriteCount;
    }
}

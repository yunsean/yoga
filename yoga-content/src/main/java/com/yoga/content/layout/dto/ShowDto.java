package com.yoga.content.layout.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class ShowDto extends TenantDto {

    private Long listLayoutId;
    private Long detailLayoutId;
    private Long columnId;
    private String articleId;

    public Long getListLayoutId() {
        return listLayoutId;
    }
    public void setListLayoutId(Long listLayoutId) {
        this.listLayoutId = listLayoutId;
    }

    public Long getDetailLayoutId() {
        return detailLayoutId;
    }
    public void setDetailLayoutId(Long detailLayoutId) {
        this.detailLayoutId = detailLayoutId;
    }

    public Long getColumnId() {
        return columnId;
    }
    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getArticleId() {
        return articleId;
    }
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}

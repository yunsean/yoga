package com.yoga.content.article.service;

import com.yoga.core.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HotKeywordService extends BaseService {
	
    public void hitKeywords(String[] keywords) {
		if (keywords != null) {
			for (String keyword : keywords) {
				redisOperator.zadd("hotkeywords", keyword, 1);
			}
		}
	}
    public Set<String> hotestKeywords(int count) {
		Set<String> hotest = redisOperator.zrange("hotkeywords", count);
		return hotest;
	}
}

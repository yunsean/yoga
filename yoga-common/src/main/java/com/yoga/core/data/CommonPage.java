package com.yoga.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

public class CommonPage implements Serializable {
	private static final long serialVersionUID = 7449150234491211106L;

	public CommonPage(Page<?> page) {
		pageSize = page.getSize();
		pageIndex = page.getNumber();
		pageCount = page.getTotalPages();
		totalCount = page.getTotalElements();
	}
	public CommonPage(){
		this.pageIndex = 0;
		this.pageSize = defaultPageSize();
	}
	public CommonPage(int pageIndex, int pageSize){
		this.pageIndex = pageIndex < 0 ? 0 : pageIndex;
		this.pageSize = pageSize < 0 ? defaultPageSize() : pageSize;
	}
	public CommonPage(int pageIndex, int pageSize, long totalCount){
		this.pageIndex = pageIndex < 0 ? 0 : pageIndex;
		this.pageSize = pageSize <= 0 ? defaultPageSize() : pageSize;
		this.totalCount = totalCount;
		int lastPageElements= (int)(this.totalCount%this.pageSize);
		this.pageCount = (int)(this.totalCount / this.pageSize) + (lastPageElements > 0 ? 1 : 0);
	}
	public CommonPage(CommonPage page, long totalCount){
		this.pageIndex = page.getPageIndex();
		this.pageSize = page.getPageSize();
		this.totalCount = totalCount;
		int lastPageElements= (int)(this.totalCount%this.pageSize);
		this.pageCount = (int)(this.totalCount / this.pageSize) + (lastPageElements > 0 ? 1 : 0);
	}

	private int pageIndex = 0;
	private int pageSize = 0;
	private int pageCount = 0;
	private long totalCount = 0;

	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public PageRequest asPageRequest(){
		return new PageRequest(this.pageIndex, this.pageSize);
	}
	
	public PageRequest asPageRequest(Sort sort){
		return new PageRequest(this.pageIndex, this.pageSize, sort);
	}
	
	public boolean hasRequestIndex(){
		if(totalCount < 1){
			return false;
		}
		if(pageCount < pageIndex){
			return false;
		}
		return true;
	}


	protected int defaultPageSize() {
		return 15;
	}
}

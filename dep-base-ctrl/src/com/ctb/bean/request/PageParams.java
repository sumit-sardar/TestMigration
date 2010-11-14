package com.ctb.bean.request; 

import java.io.Serializable;

/**
 * Contains paging parameters, which are
 * applied to the data set returned by a platform call
 * to produce a single page of a result list
 * 
 * @author Nate_Cohen
 */
public class PageParams implements Serializable
{
    static final long serialVersionUID = 1L;
    private int pageRequested;
    private int pageSize;
    private int maxPages;
    
    /**
     * Creates a paging parameter set
     * @param pageRequested - the page of data to return
     * @param pageSize - the number of items on each page
     * @param maxPages - the maximum number of pages to obtain from data store
     */
    public PageParams (int pageRequested, int pageSize, int maxPages) {
        this.pageRequested = pageRequested;
        this.pageSize = pageSize;
        this.maxPages = maxPages;
    }
    
    /**
     * Default constructor
     *
     */
    public PageParams () {
    }
    
    /**
	 * @return Returns the maxPages.
	 */
	public int getMaxPages() {
		return maxPages;
	}
	/**
	 * @param maxPages The maxPages to set.
	 */
	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}
	/**
	 * @return Returns the pageRequested.
	 */
	public int getPageRequested() {
		return pageRequested;
	}
	/**
	 * @param pageRequested The pageRequested to set.
	 */
	public void setPageRequested(int pageRequested) {
		this.pageRequested = pageRequested;
	}
	/**
	 * @return Returns the pageSize.
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize The pageSize to set.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
} 

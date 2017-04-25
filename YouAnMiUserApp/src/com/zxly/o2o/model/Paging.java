package com.zxly.o2o.model;
/**  
 * @author dsnx
 * @version 创建时间：2015-1-6 下午7:14:26  
 * 类说明: 
 */
public class Paging {

	private int pageIndex;
	private int  pageSize;
	private String sort;
	public Paging(int pageIndex)
	{
		this.pageIndex=pageIndex;
		this.pageSize=20;
	}
	public Paging(int pageIndex,String sort)
	{
		this.pageIndex=pageIndex;
		this.pageSize=20;
		this.sort=sort;
	}
	public Paging(int pageIndex,int pageSize)
	{
		this.pageIndex=pageIndex;
		this.pageSize=pageSize;
	}

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
	
	
}

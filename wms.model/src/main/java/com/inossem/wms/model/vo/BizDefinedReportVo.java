package com.inossem.wms.model.vo;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizDefinedReport;
import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class BizDefinedReportVo extends BizDefinedReport implements IPageCommon {
	
	private String definedTypeName;
	
	List<Map<String,Object>> searchField;
	List<Map<String,Object>> queryField;
	
	
	private boolean paging = false;
    private int pageSize;
    private int pageIndex;
    private int totalCount = -1;
    private boolean sortAscend;
    private String sortColumn;
    
    
	public String getDefinedTypeName() {
		return definedTypeName;
	}
	public void setDefinedTypeName(String definedTypeName) {
		this.definedTypeName = definedTypeName;
	}
	public List<Map<String, Object>> getSearchField() {
		return searchField;
	}
	public void setSearchField(List<Map<String, Object>> searchField) {
		this.searchField = searchField;
	}
	public List<Map<String, Object>> getQueryField() {
		return queryField;
	}
	public void setQueryField(List<Map<String, Object>> queryField) {
		this.queryField = queryField;
	}
	public boolean isPaging() {
		return paging;
	}
	public void setPaging(boolean paging) {
		this.paging = paging;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public boolean isSortAscend() {
		return sortAscend;
	}
	public void setSortAscend(boolean sortAscend) {
		this.sortAscend = sortAscend;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	@Override
	public void setSortAscend(String sortAscend) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<PageAggregate> getAggregateColumns() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setAggregateColumns(List<PageAggregate> aggregateColumns) {
		// TODO Auto-generated method stub
		
	}
	
	

}

package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class BizStockInputHeadVo extends BizStockInputHead implements IPageCommon {

	// 是否分页
	private boolean paging = false;
	private int pageSize;
	private int pageIndex;
	private int totalCount = -1;

	private boolean sortAscend;
	private String sortColumn;

	@Override
	public boolean isSortAscend() {
		return sortAscend;
	}

	@Override
	public void setSortAscend(boolean sortAscend) {
		this.sortAscend = sortAscend;
	}

	@Override
	public void setSortAscend(String sortAscend) {
		if ("asc".equalsIgnoreCase(sortAscend.trim())) {
			this.sortAscend = true;
		} else {
			this.sortAscend = false;
		}
	}

	@Override
	public String getSortColumn() {
		return sortColumn;
	}

	@Override
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	@Override
	public boolean isPaging() {
		return paging;
	}

	@Override
	public void setPaging(boolean paging) {
		this.paging = paging;
	}

	@Override
	public int getPageIndex() {
		return pageIndex;
	}

	@Override
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

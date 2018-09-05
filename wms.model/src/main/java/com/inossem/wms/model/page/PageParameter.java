package com.inossem.wms.model.page;

public class PageParameter {
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

	public int pageIndex = 1;
	public int pageSize = 20;
	public int totalCount = 0;
	public boolean sortAscend = true;
	public String sortColumn = "";
}

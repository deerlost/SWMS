package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.biz.BizStockOutputReturnHead;
import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class BizStockOutputReturnHeadVo extends BizStockOutputReturnHead implements IPageCommon {

	private String matDocCode;
	
	private String mesDocCode;
	
	
	public String getMatDocCode() {
		return matDocCode;
	}

	public void setMatDocCode(String matDocCode) {
		this.matDocCode = matDocCode;
	}

	public String getMesDocCode() {
		return mesDocCode;
	}

	public void setMesDocCode(String mesDocCode) {
		this.mesDocCode = mesDocCode;
	}

	private String statusStr;

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	private String stockOutputCode;
	private String ftyName;
	private String deptName;
	private String matTypeName;
	private String userName;

	private String matReqCode;

	public String getMatReqCode() {
		return matReqCode;
	}

	public void setMatReqCode(String matReqCode) {
		this.matReqCode = matReqCode;
	}

	public String getStockOutputCode() {
		return stockOutputCode;
	}

	public void setStockOutputCode(String stockOutputCode) {
		this.stockOutputCode = stockOutputCode;
	}

	public String getFtyName() {
		return ftyName;
	}

	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getMatTypeName() {
		return matTypeName;
	}

	public void setMatTypeName(String matTypeName) {
		this.matTypeName = matTypeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private boolean paging = false;
	private int pageSize;
	private int pageIndex;
	private int totalCount = -1;
	private boolean sortAscend;
	private String sortColumn;

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

	private String isFinishSql;

	private String condition;

	public String getIsFinishSql() {
		return isFinishSql;
	}

	public void setIsFinishSql(String isFinishSql) {
		this.isFinishSql = isFinishSql;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

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
	public List<PageAggregate> getAggregateColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAggregateColumns(List<PageAggregate> aggregateColumns) {
		// TODO Auto-generated method stub
		
	}
}

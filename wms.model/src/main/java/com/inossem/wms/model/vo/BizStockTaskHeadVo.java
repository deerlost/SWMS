package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.biz.BizStockTaskHead;
import com.inossem.wms.model.biz.BizStockTaskHeadCw;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class BizStockTaskHeadVo extends BizStockTaskHeadCw implements IPageCommon {

	private String statusString;

	
	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	private String receiptTypeString;

	public String getReceiptTypeString() {
		return receiptTypeString;
	}

	public void setReceiptTypeString(String receiptTypeString) {
		this.receiptTypeString = receiptTypeString;
	}

	private boolean paging = false;
	private int pageSize;
	private int pageIndex;
	private int totalCount = -1;

	private boolean sortAscend;
	private String sortColumn;

	private String condition;

	private String locationName;
	private String ftyName;
	private String userName;

	private String whCode;
	private String whName;
	private String ftyCode;

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getWhName() {
		return whName;
	}

	public void setWhName(String whName) {
		this.whName = whName;
	}

	public String getFtyCode() {
		return ftyCode;
	}

	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}

	private String locationCode;

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getFtyName() {
		return ftyName;
	}

	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

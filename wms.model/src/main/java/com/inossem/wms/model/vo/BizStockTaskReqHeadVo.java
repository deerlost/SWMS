package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class BizStockTaskReqHeadVo extends BizStockTaskReqHead implements IPageCommon {

	private String locationCode;
	private String locationName;
	private String whCode;
	private String whName;
	private String createUserName;
	private int inputId;

	private int matDocType;
	private String matDocCode;
	
	public String getMatDocCode() {
		return matDocCode;
	}

	public void setMatDocCode(String matDocCode) {
		this.matDocCode = matDocCode;
	}

	private String referReceiptCode;

	public String getReferReceiptCode() {
		return referReceiptCode;
	}

	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode;
	}

	public int getMatDocType() {
		return matDocType;
	}

	public String getMatDocTypeName() {
		return EnumReceiptType.getNameByValue((byte) (this.getMatDocType()));
	}

	public void setMatDocType(int matDocType) {
		this.matDocType = matDocType;
	}

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

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public int getInputId() {
		return inputId;
	}

	public void setInputId(int inputId) {
		this.inputId = inputId;
	}

	private String condition;
	private String isFinishSql;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getIsFinishSql() {
		return isFinishSql;
	}

	public void setIsFinishSql(String isFinishSql) {
		this.isFinishSql = isFinishSql;
	}

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

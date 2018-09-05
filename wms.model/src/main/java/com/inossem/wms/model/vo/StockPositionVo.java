package com.inossem.wms.model.vo;

import com.inossem.wms.model.stock.StockPosition;

public class StockPositionVo extends StockPosition {
	private int snId;

	public int getSnId() {
		return snId;
	}

	public void setSnId(int snId) {
		this.snId = snId;
	}
	private boolean paging = false;
	private int pageSize;
	private int pageIndex;
	private int totalCount = -1;
	
	private String nameEn;
	private String nameZh;
	
	private String sql;


	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	

	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getNameZh() {
		return nameZh;
	}
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
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
	private String matCode;
	private String matName;
	private String areaCode;
	private String positionCode;
	private Byte decimalPlace;

	private String whCode;


	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getPositionCode() {
		return positionCode;
	}
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	public Byte getDecimalPlace() {
		return decimalPlace;
	}
	public void setDecimalPlace(Byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}
	
	
	
	
	
}

package com.inossem.wms.model.vo;

import java.math.BigDecimal;

public class BizStockTaskItemCwVo{
	private Integer stockTaskId;
	private String stockTaskCode;
	private BigDecimal qty;
	private Integer unitId;
	private String unitCode;
	private String unitName;
	private String createUser;
	private String createTime;
	public Integer getStockTaskId() {
		return stockTaskId;
	}
	public void setStockTaskId(Integer stockTaskId) {
		this.stockTaskId = stockTaskId;
	}
	public String getStockTaskCode() {
		return stockTaskCode;
	}
	public void setStockTaskCode(String stockTaskCode) {
		this.stockTaskCode = stockTaskCode;
	}
	
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}

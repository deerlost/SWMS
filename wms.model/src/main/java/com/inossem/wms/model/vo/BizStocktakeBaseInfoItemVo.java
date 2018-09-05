package com.inossem.wms.model.vo;

import java.math.BigDecimal;

/**
 * 2.6	根据盘点凭证号获取盘点凭证基本信息 返回VO的ItemList结构
 * @author wlg
 *
 */
public class BizStocktakeBaseInfoItemVo {
	private Integer areaId;
	private String areaCode;
	private String areaName;
	private Integer positionCount;  // 仓位数
	private Integer matCount;// 物料品类数
	private BigDecimal stocktakedCount;//已盘点数
	private String stocktakeUsers;// 盘点人可复数
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getPositionCount() {
		return positionCount;
	}
	public void setPositionCount(Integer positionCount) {
		this.positionCount = positionCount;
	}
	public Integer getMatCount() {
		return matCount;
	}
	public void setMatCount(Integer matCount) {
		this.matCount = matCount;
	}
	public BigDecimal getStocktakedCount() {
		return stocktakedCount;
	}
	public void setStocktakedCount(BigDecimal stocktakedCount) {
		this.stocktakedCount = stocktakedCount;
	}
	public String getStocktakeUsers() {
		return stocktakeUsers;
	}
	public void setStocktakeUsers(String stocktakeUsers) {
		this.stocktakeUsers = stocktakeUsers;
	}
	
	
}

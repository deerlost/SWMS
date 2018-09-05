package com.inossem.wms.model.vo;

import java.util.Date;

public class StocktakeSaveInputHeadVo {
	private Integer stocktakeId;
	

    private String stocktakeCode;

    private String beginDate;
    private String endDate;

    private Byte stocktakeType;

    private String remark;

    private Integer ftyId;

    private Integer locationId;

    private Byte positionStatus;

    private Byte stockStatus;

    private String specStockCode;

    private String specStockName;

//    private Date submitDate;

    private Byte status;

    private Byte freezeStocktake;
  private String createUser;
	public Integer getStocktakeId() {
		return stocktakeId;
	}

	public void setStocktakeId(Integer stocktakeId) {
		this.stocktakeId = stocktakeId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Byte getStocktakeType() {
		return stocktakeType;
	}

	public void setStocktakeType(Byte stocktakeType) {
		this.stocktakeType = stocktakeType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getFtyId() {
		return ftyId;
	}

	public void setFtyId(Integer ftyId) {
		this.ftyId = ftyId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Byte getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(Byte positionStatus) {
		this.positionStatus = positionStatus;
	}

	public Byte getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(Byte stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getSpecStockCode() {
		return specStockCode;
	}

	public void setSpecStockCode(String specStockCode) {
		this.specStockCode = specStockCode;
	}

	public String getSpecStockName() {
		return specStockName;
	}

	public void setSpecStockName(String specStockName) {
		this.specStockName = specStockName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getFreezeStocktake() {
		return freezeStocktake;
	}

	public void setFreezeStocktake(Byte freezeStocktake) {
		this.freezeStocktake = freezeStocktake;
	}

	public String getStocktakeCode() {
		return stocktakeCode;
	}

	public void setStocktakeCode(String stocktakeCode) {
		this.stocktakeCode = stocktakeCode;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


    
    
}

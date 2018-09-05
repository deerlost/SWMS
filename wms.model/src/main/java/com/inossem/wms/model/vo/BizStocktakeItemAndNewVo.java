package com.inossem.wms.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BizStocktakeItemAndNewVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer itemId;

    private Integer stocktakeId;
    
    private String stocktakeCode;

    private Integer whId;
    private String whCode;
    

    private Integer areaId;
    private String areaCode;

    private Integer positionId;
    private String positionCode;
//
//    private String prefix;
//
//    private String suffix;

    private Integer matId;
    private String matCode;
    private String matName;

    private Long batch;

    private BigDecimal stockQty;

    private BigDecimal stocktakeQty;

    private Integer unitId;
    private String unitName;

    private String specStock;

    private String specStockCode;

    private String specStockName;
    

    private String takeDeliveryDate;

    private String actualStocktakeTime;

    private String stocktakeUser;

    private String remark;

    private Byte status;
    
    private BigDecimal lastQty;
    private String stocktakeUsers; // 盘点人名称
    private boolean stocktakeUsersIsHas; // 是否存在盘点人
    
    private BizStoktakeItemNewVo itemNew = new BizStoktakeItemNewVo();
    private Byte decimalPlace;
    private BigDecimal price;  // 库存单价：调用SAP接口获取移动平均价
    
    public BizStocktakeItemAndNewVo() {
    	
    }

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStocktakeId() {
		return stocktakeId;
	}

	public void setStocktakeId(Integer stocktakeId) {
		this.stocktakeId = stocktakeId;
	}

	public Integer getWhId() {
		return whId;
	}

	public void setWhId(Integer whId) {
		this.whId = whId;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

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

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
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

	public Long getBatch() {
		return batch;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
	}

	public BigDecimal getStockQty() {
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty) {
		this.stockQty = stockQty;
	}

	public BigDecimal getStocktakeQty() {
		return stocktakeQty;
	}

	public void setStocktakeQty(BigDecimal stocktakeQty) {
		this.stocktakeQty = stocktakeQty;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
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

	public String getTakeDeliveryDate() {
		return takeDeliveryDate;
	}

	public void setTakeDeliveryDate(String takeDeliveryDate) {
		this.takeDeliveryDate = takeDeliveryDate;
	}

	public String getActualStocktakeTime() {
		return actualStocktakeTime;
	}

	public void setActualStocktakeTime(String actualStocktakeTime) {
		this.actualStocktakeTime = actualStocktakeTime;
	}

	public String getStocktakeUser() {
		return stocktakeUser;
	}

	public void setStocktakeUser(String stocktakeUser) {
		this.stocktakeUser = stocktakeUser;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public BigDecimal getLastQty() {
		return lastQty;
	}

	public void setLastQty(BigDecimal lastQty) {
		this.lastQty = lastQty;
	}

	public String getStocktakeUsers() {
		return stocktakeUsers;
	}

	public void setStocktakeUsers(String stocktakeUsers) {
		this.stocktakeUsers = stocktakeUsers;
	}

	public boolean isStocktakeUsersIsHas() {
		return stocktakeUsersIsHas;
	}

	public void setStocktakeUsersIsHas(boolean stocktakeUsersIsHas) {
		this.stocktakeUsersIsHas = stocktakeUsersIsHas;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Byte getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(Byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	public BizStoktakeItemNewVo getItemNew() {
		return itemNew;
	}

	public void setItemNew(BizStoktakeItemNewVo itemNew) {
		this.itemNew = itemNew;
	}

	public String getStocktakeCode() {
		return stocktakeCode;
	}

	public void setStocktakeCode(String stocktakeCode) {
		this.stocktakeCode = stocktakeCode;
	}

    
}

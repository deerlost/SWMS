package com.inossem.wms.model.stock;

import java.math.BigDecimal;
import java.util.Date;

public class StockPositionHistory {
	
	private Integer packageTypeId;
	private Byte stockTypeId;
	
	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Byte getStockTypeId() {
		return stockTypeId;
	}

	public void setStockTypeId(Byte stockTypeId) {
		this.stockTypeId = stockTypeId;
	}

	private Byte isUrgent; 
	
	public Byte getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(Byte isUrgent) {
		this.isUrgent = isUrgent;
	}

	private Integer id;

	private Integer stockId;

	private Integer whId;

	private Integer areaId;

	private Integer positionId;

	private Integer palletId;

	private Integer packageId;

	private Integer matId;

	private Integer ftyId;

	private Integer locationId;

	private Long batch;

	private Byte status;

	private BigDecimal qty;

	private String specStock;

	private String specStockCode;

	private String specStockName;

	private Date inputDate;

	private Date takeDeliveryDate;

	private Integer unitId;

	private Integer unitWeight;

	private Date createTime;

	private Date modifyTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public Integer getWhId() {
		return whId;
	}

	public void setWhId(Integer whId) {
		this.whId = whId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public Integer getPalletId() {
		return palletId;
	}

	public void setPalletId(Integer palletId) {
		this.palletId = palletId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
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

	public Long getBatch() {
		return batch;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock == null ? null : specStock.trim();
	}

	public String getSpecStockCode() {
		return specStockCode;
	}

	public void setSpecStockCode(String specStockCode) {
		this.specStockCode = specStockCode == null ? null : specStockCode.trim();
	}

	public String getSpecStockName() {
		return specStockName;
	}

	public void setSpecStockName(String specStockName) {
		this.specStockName = specStockName == null ? null : specStockName.trim();
	}

	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public Date getTakeDeliveryDate() {
		return takeDeliveryDate;
	}

	public void setTakeDeliveryDate(Date takeDeliveryDate) {
		this.takeDeliveryDate = takeDeliveryDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(Integer unitWeight) {
		this.unitWeight = unitWeight;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public StockPositionHistory(StockPosition stockPosition) {

		this.packageTypeId = stockPosition.getPackageTypeId();
		
		this.stockTypeId = stockPosition.getStockTypeId();
		
		this.isUrgent = stockPosition.getIsUrgent();
		
		this.stockId = stockPosition.getId();

		this.whId = stockPosition.getWhId();

		this.areaId = stockPosition.getAreaId();

		this.positionId = stockPosition.getPositionId();

		this.palletId = stockPosition.getPalletId();

		this.packageId = stockPosition.getPackageId();

		this.matId = stockPosition.getMatId();

		this.ftyId = stockPosition.getFtyId();

		this.locationId = stockPosition.getLocationId();

		this.batch = stockPosition.getBatch();

		this.status = stockPosition.getStatus();

		this.qty = stockPosition.getQty();

		this.specStock = stockPosition.getSpecStock();

		this.specStockCode = stockPosition.getSpecStockCode();

		this.specStockName = stockPosition.getSpecStockName();

		this.inputDate = stockPosition.getInputDate();

		this.takeDeliveryDate = stockPosition.getTakeDeliveryDate();

		this.unitId = stockPosition.getUnitId();

		this.unitWeight = stockPosition.getUnitWeight();

		this.createTime = stockPosition.getCreateTime();

		// this.modifyTime = stockPosition.getModifyTime();
	}
}
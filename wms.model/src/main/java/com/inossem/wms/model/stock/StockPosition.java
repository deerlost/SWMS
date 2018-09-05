package com.inossem.wms.model.stock;

import java.math.BigDecimal;
import java.util.Date;

import com.inossem.wms.model.key.StockPositionKey;

public class StockPosition extends StockPositionKey {

	// --------------
	// 是否紧急记账
	private Byte isUrgent; 
	
	private String areaName;
	private Integer packageTypeId;
	
	private String unitZh;
	private String packageTypeCode;
	
	
	private String packageCode;
	private String palletCode;
	private BigDecimal maxWeight;
	private String whCode;
	private String matCode;
	private String matName;
	private String ftyCode;
	private String ftyName;
	private String locationCode;
	private String locationName;
	private String areaCode;
	private String positionCode;
	private String debitOrCredit;
	
	
	// --------------

	
	
	public String getDebitOrCredit() {
		return debitOrCredit;
	}

	public Byte getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(Byte isUrgent) {
		this.isUrgent = isUrgent;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public String getUnitZh() {
		return unitZh;
	}

	public void setUnitZh(String unitZh) {
		this.unitZh = unitZh;
	}

	public String getPackageTypeCode() {
		return packageTypeCode;
	}

	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPalletCode() {
		return palletCode;
	}

	public void setPalletCode(String palletCode) {
		this.palletCode = palletCode;
	}

	public BigDecimal getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(BigDecimal maxWeight) {
		this.maxWeight = maxWeight;
	}

	public void setDebitOrCredit(String debitOrCredit) {
		this.debitOrCredit = debitOrCredit;
	}

	private Integer id;

	// public Integer getPalletId() {
	// return palletId;
	// }
	//
	// public void setPalletId(Integer palletId) {
	// this.palletId = palletId;
	// }
	//
	// public Integer getPackageId() {
	// return packageId;
	// }
	//
	// public void setPackageId(Integer packageId) {
	// this.packageId = packageId;
	// }

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

	public String getFtyCode() {
		return ftyCode;
	}

	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}

	public String getFtyName() {
		return ftyName;
	}

	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
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

	private Integer whId;

	private Integer areaId;

	// private Integer positionId;
	//
	// private Integer palletId;
	//
	// private Integer packageId;
	//
	// private Integer matId;

	private Integer ftyId;

	// private Integer locationId;
	//
	// private Long batch;

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

	// public Integer getPositionId() {
	// return positionId;
	// }
	//
	// public void setPositionId(Integer positionId) {
	// this.positionId = positionId;
	// }
	//
	// public Integer getMatId() {
	// return matId;
	// }
	//
	// public void setMatId(Integer matId) {
	// this.matId = matId;
	// }

	public Integer getFtyId() {
		return ftyId;
	}

	public void setFtyId(Integer ftyId) {
		this.ftyId = ftyId;
	}

	// public Integer getLocationId() {
	// return locationId;
	// }
	//
	// public void setLocationId(Integer locationId) {
	// this.locationId = locationId;
	// }
	//
	// public Long getBatch() {
	// return batch;
	// }
	//
	// public void setBatch(Long batch) {
	// this.batch = batch;
	// }

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
}
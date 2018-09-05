package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockInputTransportItem {
	private Integer itemId;

	private Integer stockTransportId;

	private Integer stockTransportRid;

	private Integer matId;

	private Integer ftyId;

	private Integer ftyOutputId;

	private Integer locationId;

	private Integer locationOutputId;

	private Long batch;

	private Integer whId;

	private Integer areaId;

	private BigDecimal transportQty;
	
	private String mesFailCode;
	
	public BigDecimal getStockQty() {
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty) {
		this.stockQty = stockQty;
	}

	private BigDecimal stockQty;;

	private Integer ftyInput;

	private Integer locationInput;

	private Integer matInput;

	private Integer unitId;

	private Date createTime;

	private Date modifyTime;

	private Byte isDelete;

	private String referReceiptCode;

	private Integer referReceiptRid;

	private Integer businessType;

	private String remark;

	private String productionBatch;

	private String erpBatch;

	private Integer referReceiptId;

	private Integer matInputRid;

	private Integer packageTypeId;

	private Integer matDocRid;
	private Integer matDocId;
	private String mesDocCode;

	public Integer getMatDocRid() {
		return matDocRid;
	}

	public void setMatDocRid(Integer matDocRid) {
		this.matDocRid = matDocRid;
	}

	public Integer getMatDocId() {
		return matDocId;
	}

	public void setMatDocId(Integer matDocId) {
		this.matDocId = matDocId;
	}

	public String getMatDocYear() {
		return matDocYear;
	}

	public void setMatDocYear(String matDocYear) {
		this.matDocYear = matDocYear;
	}

	public String getMatDocCode() {
		return matDocCode;
	}

	public void setMatDocCode(String matDocCode) {
		this.matDocCode = matDocCode;
	}

	private String matDocYear;
	private String matDocCode;
	private String matOffCode;

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	private String matYear;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStockTransportId() {
		return stockTransportId;
	}

	public void setStockTransportId(Integer stockTransportId) {
		this.stockTransportId = stockTransportId;
	}

	public Integer getStockTransportRid() {
		return stockTransportRid;
	}

	public void setStockTransportRid(Integer stockTransportRid) {
		this.stockTransportRid = stockTransportRid;
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

	public Integer getFtyOutputId() {
		return ftyOutputId;
	}

	public void setFtyOutputId(Integer ftyOutputId) {
		this.ftyOutputId = ftyOutputId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getLocationOutputId() {
		return locationOutputId;
	}

	public void setLocationOutputId(Integer locationOutputId) {
		this.locationOutputId = locationOutputId;
	}

	public Long getBatch() {
		return batch;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
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

	public BigDecimal getTransportQty() {
		return transportQty;
	}

	public void setTransportQty(BigDecimal transportQty) {
		this.transportQty = transportQty;
	}

	public Integer getFtyInput() {
		return ftyInput;
	}

	public void setFtyInput(Integer ftyInput) {
		this.ftyInput = ftyInput;
	}

	public Integer getLocationInput() {
		return locationInput;
	}

	public void setLocationInput(Integer locationInput) {
		this.locationInput = locationInput;
	}

	public Integer getMatInput() {
		return matInput;
	}

	public void setMatInput(Integer matInput) {
		this.matInput = matInput;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	public String getReferReceiptCode() {
		return referReceiptCode;
	}

	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode == null ? null : referReceiptCode.trim();
	}

	public Integer getReferReceiptRid() {
		return referReceiptRid;
	}

	public void setReferReceiptRid(Integer referReceiptRid) {
		this.referReceiptRid = referReceiptRid;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getProductionBatch() {
		return productionBatch;
	}

	public void setProductionBatch(String productionBatch) {
		this.productionBatch = productionBatch;
	}

	public String getErpBatch() {
		return erpBatch;
	}

	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}

	public Integer getReferReceiptId() {
		return referReceiptId;
	}

	public void setReferReceiptId(Integer referReceiptId) {
		this.referReceiptId = referReceiptId;
	}

	public String getMatYear() {
		return matYear;
	}

	public void setMatYear(String matYear) {
		this.matYear = matYear;
	}

	public Integer getMatInputRid() {
		return matInputRid;
	}

	public void setMatInputRid(Integer matInputRid) {
		this.matInputRid = matInputRid;
	}

	public String getMesDocCode() {
		return mesDocCode;
	}

	public void setMesDocCode(String mesDocCode) {
		this.mesDocCode = mesDocCode;
	}

	public String getMesFailCode() {
		return mesFailCode;
	}

	public void setMesFailCode(String mesFailCode) {
		this.mesFailCode = mesFailCode;
	}

	public String getMatOffCode() {
		return matOffCode;
	}

	public void setMatOffCode(String matOffCode) {
		this.matOffCode = matOffCode;
	}

}
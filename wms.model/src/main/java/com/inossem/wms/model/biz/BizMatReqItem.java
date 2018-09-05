package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizMatReqItem {
	private Integer itemId;

	private Integer matReqId;

	private Integer matReqRid;

	private Integer ftyId;

	private Integer locationId;

	private Integer moveTypeId;

	private Integer matId;

	private Integer unitId;

	private BigDecimal referPrice;

	private BigDecimal demandQty;

	private Date demandDate;

	private BigDecimal takeDeliveryQty;

	private String costObjCode;

	private String costObjName;

	private String generalLedgerSubject;

	private String workReceiptCode;

	private String workReceiptName;

	private String deviceName;

	private String deviceCode;

	private Byte assetAttr;

	private String remark;

	private String reserveCode;

	private String reserveRid;

	private String purchaseOrderCode;

	private String purchaseOrderRid;

	private Byte isDelete;

	private Date createTime;

	private Date modifyTime;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getMatReqId() {
		return matReqId;
	}

	public void setMatReqId(Integer matReqId) {
		this.matReqId = matReqId;
	}

	public Integer getMatReqRid() {
		return matReqRid;
	}

	public void setMatReqRid(Integer matReqRid) {
		this.matReqRid = matReqRid;
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

	public Integer getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(Integer moveTypeId) {
		this.moveTypeId = moveTypeId;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public BigDecimal getReferPrice() {
		return referPrice;
	}

	public void setReferPrice(BigDecimal referPrice) {
		this.referPrice = referPrice;
	}

	public BigDecimal getDemandQty() {
		return demandQty;
	}

	public void setDemandQty(BigDecimal demandQty) {
		this.demandQty = demandQty;
	}

	public Date getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(Date demandDate) {
		this.demandDate = demandDate;
	}

	public BigDecimal getTakeDeliveryQty() {
		return takeDeliveryQty;
	}

	public void setTakeDeliveryQty(BigDecimal takeDeliveryQty) {
		this.takeDeliveryQty = takeDeliveryQty;
	}

	public String getCostObjCode() {
		return costObjCode;
	}

	public void setCostObjCode(String costObjCode) {
		this.costObjCode = costObjCode == null ? null : costObjCode.trim();
	}

	public String getCostObjName() {
		return costObjName;
	}

	public void setCostObjName(String costObjName) {
		this.costObjName = costObjName == null ? null : costObjName.trim();
	}

	public String getGeneralLedgerSubject() {
		return generalLedgerSubject;
	}

	public void setGeneralLedgerSubject(String generalLedgerSubject) {
		this.generalLedgerSubject = generalLedgerSubject == null ? null : generalLedgerSubject.trim();
	}

	public String getWorkReceiptCode() {
		return workReceiptCode;
	}

	public void setWorkReceiptCode(String workReceiptCode) {
		this.workReceiptCode = workReceiptCode == null ? null : workReceiptCode.trim();
	}

	public String getWorkReceiptName() {
		return workReceiptName;
	}

	public void setWorkReceiptName(String workReceiptName) {
		this.workReceiptName = workReceiptName == null ? null : workReceiptName.trim();
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName == null ? null : deviceName.trim();
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode == null ? null : deviceCode.trim();
	}

	public Byte getAssetAttr() {
		return assetAttr;
	}

	public void setAssetAttr(Byte assetAttr) {
		this.assetAttr = assetAttr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getReserveCode() {
		return reserveCode;
	}

	public void setReserveCode(String reserveCode) {
		this.reserveCode = reserveCode == null ? null : reserveCode.trim();
	}

	public String getReserveRid() {
		return reserveRid;
	}

	public void setReserveRid(String reserveRid) {
		this.reserveRid = reserveRid == null ? null : reserveRid.trim();
	}

	public String getPurchaseOrderCode() {
		return purchaseOrderCode;
	}

	public void setPurchaseOrderCode(String purchaseOrderCode) {
		this.purchaseOrderCode = purchaseOrderCode == null ? null : purchaseOrderCode.trim();
	}

	public String getPurchaseOrderRid() {
		return purchaseOrderRid;
	}

	public void setPurchaseOrderRid(String purchaseOrderRid) {
		this.purchaseOrderRid = purchaseOrderRid == null ? null : purchaseOrderRid.trim();
	}

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
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
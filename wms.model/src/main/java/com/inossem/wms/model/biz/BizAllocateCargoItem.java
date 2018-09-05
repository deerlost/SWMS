package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizAllocateCargoItem {
    private Integer itemId;

	private Integer allocateCargoId;

	private Integer allocateCargoRid;

	private Integer ftyId;

	private Integer locationId;

	private Integer matId;

	private Integer unitId;

	private Byte decimalPlace;

	private BigDecimal orderQty;

	private BigDecimal outputQty;

	private String referReceiptRid;

	private String remark;

	private Byte isDelete;

	private String createUser;

	private String modifyUser;

	private Date createTime;

	private Date modifyTime;

	private String erpRemark;
	
	private String erpBatch;
	

	public String getErpBatch() {
		return erpBatch;
	}

	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAllocateCargoId() {
		return allocateCargoId;
	}

	public void setAllocateCargoId(Integer allocateCargoId) {
		this.allocateCargoId = allocateCargoId;
	}

	public Integer getAllocateCargoRid() {
		return allocateCargoRid;
	}

	public void setAllocateCargoRid(Integer allocateCargoRid) {
		this.allocateCargoRid = allocateCargoRid;
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

	public Byte getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(Byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}

	public BigDecimal getOutputQty() {
		return outputQty;
	}

	public void setOutputQty(BigDecimal outputQty) {
		this.outputQty = outputQty;
	}

	public String getReferReceiptRid() {
		return referReceiptRid;
	}

	public void setReferReceiptRid(String referReceiptRid) {
		this.referReceiptRid = referReceiptRid == null ? null : referReceiptRid.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser == null ? null : modifyUser.trim();
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

	public String getErpRemark() {
		return erpRemark;
	}

	public void setErpRemark(String erpRemark) {
		this.erpRemark = erpRemark == null ? null : erpRemark.trim();
	}

}
package com.inossem.wms.model.biz;

import java.util.Date;

import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.page.IPageCommon;

public class BizStockTaskHead {
	private Byte requestSource;

	private Integer stockTaskId;

	private String stockTaskCode;

	private Byte receiptType;

	private Integer whId;

	private Integer moveTypeId;

	private String shippingType;

	private Byte status;

	private Date submitDate;

	private Integer stockTaskReqId;

	private Integer ftyId;

	private Integer locationId;

	private String remark;

	private String createUser;

	private Byte isDelete;

	private Date createTime;

	private Date modifyTime;

	public Byte getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Byte requestSource) {
		this.requestSource = requestSource;
	}

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
		this.stockTaskCode = stockTaskCode == null ? null : stockTaskCode.trim();
	}

	public Byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Byte receiptType) {
		this.receiptType = receiptType;
	}

	public String getReceiptTypeName() {
		return EnumReceiptType.getNameByValue(this.getReceiptType());
	}

	public Integer getWhId() {
		return whId;
	}

	public void setWhId(Integer whId) {
		this.whId = whId;
	}

	public Integer getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(Integer moveTypeId) {
		this.moveTypeId = moveTypeId;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType == null ? null : shippingType.trim();
	}

	public Byte getStatus() {
		return status;
	}

	public String getStatusNameForListing() {
		if (0 == status) {
			return "未入库";
		} else {
			return "已入库";
		}
	}
	
	public String getStatusNameForRemoval() {
		if (0 == status) {
			return "未入库";
		} else {
			return "已入库";
		}
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Integer getStockTaskReqId() {
		return stockTaskReqId;
	}

	public void setStockTaskReqId(Integer stockTaskReqId) {
		this.stockTaskReqId = stockTaskReqId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
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
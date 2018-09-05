package com.inossem.wms.model.biz;

import java.util.Date;

public class BizStockInputTransportHead {
	private Integer stockTransportId;

	private String stockTransportCode;

	private Byte receiptType;

	private Integer moveTypeId;

	private Integer ftyId;

	private Integer locationId;

	private Byte status;

	private String remark;

	private Byte requestSource;

	private Byte isDelete;

	private String createUser;

	private Date createTime;

	private Date modifyTime;

	private Integer classTypeId;

	private Integer synType;

	private Integer stockInputId;
	
	private String docTime;
	
	private String postingDate;

	
	public Integer getStockInputId() {
		return stockInputId;
	}

	public void setStockInputId(Integer stockInputId) {
		this.stockInputId = stockInputId;
	}

	public Integer getStockTransportId() {
		return stockTransportId;
	}

	public void setStockTransportId(Integer stockTransportId) {
		this.stockTransportId = stockTransportId;
	}

	public String getStockTransportCode() {
		return stockTransportCode;
	}

	public void setStockTransportCode(String stockTransportCode) {
		this.stockTransportCode = stockTransportCode == null ? null : stockTransportCode.trim();
	}

	public Byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Byte receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(Integer moveTypeId) {
		this.moveTypeId = moveTypeId;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Byte getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Byte requestSource) {
		this.requestSource = requestSource;
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

	public Integer getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(Integer classTypeId) {
		this.classTypeId = classTypeId;
	}

	public Integer getSynType() {
		return synType;
	}

	public void setSynType(Integer synType) {
		this.synType = synType;
	}

	public String getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}

	public String getDocTime() {
		return docTime;
	}

	public void setDocTime(String docTime) {
		this.docTime = docTime;
	}
}
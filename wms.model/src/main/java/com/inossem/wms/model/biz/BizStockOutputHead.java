package com.inossem.wms.model.biz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BizStockOutputHead {

	private Integer stockOutputId;

	private String stockOutputCode;

	private Byte receiptType;

	private Integer corpId;

	private Byte status;
	
	private String createUser;

	private String modifyUser;

	private Date createTime;

	private Date modifyTime;

	private String referReceiptCode;

	private String preorderId;

	private String receiveCode;

	private String receiveName;

	private String orderType;

	private String orderTypeName;

	private String orgCode;

	private String orgName;

	private String groupCode;

	private String remark;

	private Byte requestSource;

	private Byte checkAccount;

	private Byte isDelete;
	
	private String saleOrderCode;
	
	private Integer classTypeId;
	
	private Byte synType;

	private String deliveryVehicle;

	private String deliveryDriver;
	
	private String postingDate;
	
	private String docTime;
	
	private Integer deliveryVehicleId;
	
	private Integer deliveryDriverId;
	
	private Integer moveTypeId;
	
	public Integer getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(Integer moveTypeId) {
		this.moveTypeId = moveTypeId;
	}

	public Integer getDeliveryVehicleId() {
		return deliveryVehicleId;
	}

	public void setDeliveryVehicleId(Integer deliveryVehicleId) {
		this.deliveryVehicleId = deliveryVehicleId;
	}

	public Integer getDeliveryDriverId() {
		return deliveryDriverId;
	}

	public void setDeliveryDriverId(Integer deliveryDriverId) {
		this.deliveryDriverId = deliveryDriverId;
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

	public String getDeliveryVehicle() {
		return deliveryVehicle;
	}

	public void setDeliveryVehicle(String deliveryVehicle) {
		this.deliveryVehicle = deliveryVehicle;
	}

	public String getDeliveryDriver() {
		return deliveryDriver;
	}

	public void setDeliveryDriver(String deliveryDriver) {
		this.deliveryDriver = deliveryDriver;
	}

	public Byte getSynType() {
		return synType;
	}

	public void setSynType(Byte synType) {
		this.synType = synType;
	}

	public Integer getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(Integer classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}


	public Integer getStockOutputId() {
		return stockOutputId;
	}

	public void setStockOutputId(Integer stockOutputId) {
		this.stockOutputId = stockOutputId;
	}

	public String getStockOutputCode() {
		return stockOutputCode;
	}

	public void setStockOutputCode(String stockOutputCode) {
		this.stockOutputCode = stockOutputCode == null ? null : stockOutputCode.trim();
	}

	public Byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Byte receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getCorpId() {
		return corpId;
	}

	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getReferReceiptCode() {
		return referReceiptCode;
	}

	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode;
	}

	public String getPreorderId() {
		return preorderId;
	}

	public void setPreorderId(String preorderId) {
		this.preorderId = preorderId == null ? null : preorderId.trim();
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType == null ? null : orderType.trim();
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName == null ? null : orderTypeName.trim();
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Byte getCheckAccount() {
		return checkAccount;
	}

	public void setCheckAccount(Byte checkAccount) {
		this.checkAccount = checkAccount;
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

	public String getCreateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (createTime != null) {
			return format.format(this.createTime);
		} else {
			return null;
		}

	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (modifyTime != null) {
			return format.format(this.modifyTime);
		} else {
			return null;
		}

	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Byte getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Byte requestSource) {
		this.requestSource = requestSource;
	}


}
package com.inossem.wms.model.biz;

import java.util.Date;

public class BizAllocateCargoHead {



	private Integer allocateCargoId;

    private String allocateCargoCode;

    private Byte status;

    private String referReceiptCode;

    private String saleOrderCode;

    private String preorderId;

    private String receiveCode;

    private String receiveName;

    private String deliveryVehicle;

    private String deliveryDriver;

    private Integer classTypeId;

    private String remark;

    private String erpRemark;

    private Byte requestSource;

    private Byte isDelete;

    private String createUser;

    private String modifyUser;

    private Date createTime;

    private Date modifyTime;
    
    private Byte documentType;
    
    private String orderType;
    
    private String orderTypeName;
    
    private Integer deliveryVehicleId;
    
    private Integer deliveryDriverId;
    
    private Byte operationType;

    
    public Byte getOperationType() {
		return operationType;
	}

	public void setOperationType(Byte operationType) {
		this.operationType = operationType;
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public Byte getDocumentType() {
		return documentType;
	}

	public void setDocumentType(Byte documentType) {
		this.documentType = documentType;
	}

	public Integer getAllocateCargoId() {
        return allocateCargoId;
    }

    public void setAllocateCargoId(Integer allocateCargoId) {
        this.allocateCargoId = allocateCargoId;
    }

    public String getAllocateCargoCode() {
        return allocateCargoCode;
    }

    public void setAllocateCargoCode(String allocateCargoCode) {
        this.allocateCargoCode = allocateCargoCode == null ? null : allocateCargoCode.trim();
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
        this.referReceiptCode = referReceiptCode == null ? null : referReceiptCode.trim();
    }

    public String getSaleOrderCode() {
        return saleOrderCode;
    }

    public void setSaleOrderCode(String saleOrderCode) {
        this.saleOrderCode = saleOrderCode == null ? null : saleOrderCode.trim();
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
        this.receiveCode = receiveCode == null ? null : receiveCode.trim();
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName == null ? null : receiveName.trim();
    }

    public String getDeliveryVehicle() {
        return deliveryVehicle;
    }

    public void setDeliveryVehicle(String deliveryVehicle) {
        this.deliveryVehicle = deliveryVehicle == null ? null : deliveryVehicle.trim();
    }

    public String getDeliveryDriver() {
        return deliveryDriver;
    }

    public void setDeliveryDriver(String deliveryDriver) {
        this.deliveryDriver = deliveryDriver == null ? null : deliveryDriver.trim();
    }

    public Integer getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(Integer classTypeId) {
        this.classTypeId = classTypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getErpRemark() {
        return erpRemark;
    }

    public void setErpRemark(String erpRemark) {
        this.erpRemark = erpRemark == null ? null : erpRemark.trim();
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
}
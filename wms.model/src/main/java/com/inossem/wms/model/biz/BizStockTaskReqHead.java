package com.inossem.wms.model.biz;

import java.util.Date;

public class BizStockTaskReqHead {
	//-----------------------------
	
	private Integer removeTaskId;
	
	private String remark;
	private String deliveryVehicle; 
	private String deliveryDriver; 
	private Byte requestSource;
	private Byte referReceiptType;
	private Byte receiptType;
	private Integer referReceiptId;
	private String referReceiptCode;
	
	private Integer deliveryVehicleId;
	private Integer deliveryDriverId;
	//-----------------------------
	
	
    private Integer stockTaskReqId;

	

	public Integer getRemoveTaskId() {
		return removeTaskId;
	}

	public void setRemoveTaskId(Integer removeTaskId) {
		this.removeTaskId = removeTaskId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Byte getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Byte requestSource) {
		this.requestSource = requestSource;
	}

	public Byte getReferReceiptType() {
		return referReceiptType;
	}

	public void setReferReceiptType(Byte referReceiptType) {
		this.referReceiptType = referReceiptType;
	}

	public Byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Byte receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getReferReceiptId() {
		return referReceiptId;
	}

	public void setReferReceiptId(Integer referReceiptId) {
		this.referReceiptId = referReceiptId;
	}

	public String getReferReceiptCode() {
		return referReceiptCode;
	}

	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode;
	}

	private String stockTaskReqCode;

    private Integer whId;

    private Byte status;

    private String shippingType;

    private String createUser;

    private Integer matDocId;

    private Integer matDocYear;

    private Integer ftyId;

    private Integer locationId;

    private Date createTime;

    private Date modifyTime;

    private Byte isDelete;

    public Integer getStockTaskReqId() {
        return stockTaskReqId;
    }

    public void setStockTaskReqId(Integer stockTaskReqId) {
        this.stockTaskReqId = stockTaskReqId;
    }

    public String getStockTaskReqCode() {
        return stockTaskReqCode;
    }

    public void setStockTaskReqCode(String stockTaskReqCode) {
        this.stockTaskReqCode = stockTaskReqCode == null ? null : stockTaskReqCode.trim();
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Byte getStatus() {
        return status;
    }
    
    public String getStatusName() {
    	String statusName = "";
    	if (this.status == 0) {
    		statusName="未完成";
		}else if (this.status == 1) {
			statusName="已完成";
		}	
        return statusName;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType == null ? null : shippingType.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Integer getMatDocId() {
        return matDocId;
    }

    public void setMatDocId(Integer matDocId) {
        this.matDocId = matDocId;
    }

    public Integer getMatDocYear() {
        return matDocYear;
    }

    public void setMatDocYear(Integer matDocYear) {
        this.matDocYear = matDocYear;
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
}
package com.inossem.wms.model.biz;

import java.util.Date;
import java.util.List;

import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;

public class BizStockTaskHeadCw {
	//-------------------------
	
	private String deliveryVehicle; 
	private String deliveryDriver; 
	
	private Integer removerId;	
	private Integer forkliftWorkerId;	
	private Integer tallyClerkId;

	
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

	public Integer getRemoverId() {
		return removerId;
	}

	public void setRemoverId(Integer removerId) {
		this.removerId = removerId;
	}

	public Integer getForkliftWorkerId() {
		return forkliftWorkerId;
	}

	public void setForkliftWorkerId(Integer forkliftWorkerId) {
		this.forkliftWorkerId = forkliftWorkerId;
	}

	public Integer getTallyClerkId() {
		return tallyClerkId;
	}

	public void setTallyClerkId(Integer tallyClerkId) {
		this.tallyClerkId = tallyClerkId;
	}

	private String remover;
	private List<BizReceiptAttachment> fileList;
	private String forkliftWorker;
	private String tallyClerk;
	
	private Integer palletId;
	private Integer areaId;
	private Integer positionId;
	
	private Byte classTypeId;
	private List<BizStockTaskItemCw> itemList;
	private Byte referReceiptType;
	//---------------------------
	
    private Integer stockTaskId;

    public List<BizReceiptAttachment> getFileList() {
		return fileList;
	}

	public void setFileList(List<BizReceiptAttachment> fileList) {
		this.fileList = fileList;
	}

	public String getRemover() {
		return remover;
	}

	public void setRemover(String remover) {
		this.remover = remover;
	}

	public String getForkliftWorker() {
		return forkliftWorker;
	}

	public void setForkliftWorker(String forkliftWorker) {
		this.forkliftWorker = forkliftWorker;
	}

	public String getTallyClerk() {
		return tallyClerk;
	}

	public void setTallyClerk(String tallyClerk) {
		this.tallyClerk = tallyClerk;
	}

	public Integer getPalletId() {
		return palletId;
	}

	public void setPalletId(Integer palletId) {
		this.palletId = palletId;
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

	public Byte getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(Byte classTypeId) {
		this.classTypeId = classTypeId;
	}

	public List<BizStockTaskItemCw> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizStockTaskItemCw> itemList) {
		this.itemList = itemList;
	}

	public Byte getReferReceiptType() {
		return referReceiptType;
	}

	public void setReferReceiptType(Byte referReceiptType) {
		this.referReceiptType = referReceiptType;
	}

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

    private Byte requestSource;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    private Integer referReceiptId;

    private String referReceiptCode;

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
    
    public String getReceiptTypeName(Byte receiptType) {
    	String receipt_type_name = "";
    	if (receiptType== EnumReceiptType.STOCK_TASK_PCKAGE_CLEANUP.getValue()) {
    		receipt_type_name = EnumReceiptType.STOCK_TASK_PCKAGE_CLEANUP.getName();
		}else if (receiptType== EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getValue()) {
			receipt_type_name = EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getName();
		}else if (receiptType== EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getValue()) {
			receipt_type_name = EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getName();
		}else if (receiptType== EnumReceiptType.STOCK_TASK_MATERIAL_CLEANUP.getValue()) {
			receipt_type_name = EnumReceiptType.STOCK_TASK_MATERIAL_CLEANUP.getName();
		}
        return receipt_type_name;
    }
    
    private String receiptTypeName;
    

    public String getReceiptTypeName() {
		return receiptTypeName;
	}

	public void setReceiptTypeName(String receiptTypeName) {
		this.receiptTypeName = receiptTypeName;
	}

	public void setReceiptType(Byte receiptType) {
        this.receiptType = receiptType;
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

    public void setStatus(Byte status) {
        this.status = status;
    }
    
    public String getStatusNameForListing() {
		if (status ==null||0 == status) {
			return "未记账上架作业";
		} else {
			return "已记账上架作业";
		}
	}
    
    public String getStatusName(Byte status) {
    	String statusName = "";
		if (status == EnumReceiptStatus.RECEIPT_DRAFT.getValue()) {
			statusName = EnumReceiptStatus.RECEIPT_DRAFT.getName();
		} else if (status == EnumReceiptStatus.RECEIPT_SUBMIT.getValue()){
			statusName = EnumReceiptStatus.RECEIPT_SUBMIT.getName();
		} else if (status == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()){
			statusName = EnumReceiptStatus.RECEIPT_UNFINISH.getName();
		} else if (status == EnumReceiptStatus.RECEIPT_FINISH.getValue()){
			statusName = EnumReceiptStatus.RECEIPT_FINISH.getName();
		}
		
		return statusName;
	}
	
	public String getStatusNameForRemoval() {
		if (status ==null || 0 == status) {
			return "未记账下架作业";
		} else {
			return "已记账下架作业";
		}
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
        this.referReceiptCode = referReceiptCode == null ? null : referReceiptCode.trim();
    }
}
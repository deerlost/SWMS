package com.inossem.wms.model.biz;

import java.util.Date;
import java.util.List;

import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.page.IPageResultCommon;

public class BizStockInputHead implements IPageResultCommon{
	
	// ----------补充属性start---------
	
	private String classTypeName;
	private List<BizReceiptAttachment> fileList;
	private List<User> userList;
	//冲销原因
	private String writeOffRemark;
	
	private Integer newOtherInput;
	private String ftyCode;
	private String ftyName;
	private String locationCode;
	private String locationName;
	
	private String specStock;
	
	private String reasonCode;
	
	private String reasonName;
	
	private String corpName;

	private String moveTypeName;
	
	private String moveTypeCode;
	
	private String createUserName;
	
	private String specStockCode;
	
	
	private String classTypeId;
	private String sysStatus;
	
	private List<BizStockInputItem> itemList;
	
	private Byte requestSource;
	
	
	private String productLineId;
	private String installationId;
	private String productLineName;
	private String installationName;

	private String afpos;
	
	private Byte stckType;
	
	// ----------补充属性end---------
	
    public String getFtyCode() {
		return ftyCode;
	}

	public Byte getStckType() {
		return stckType;
	}

	public void setStckType(Byte stckType) {
		this.stckType = stckType;
	}

	public String getAfpos() {
		return afpos;
	}

	public void setAfpos(String afpos) {
		this.afpos = afpos;
	}

	public String getProductLineName() {
		return productLineName;
	}

	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}

	public String getInstallationName() {
		return installationName;
	}

	public void setInstallationName(String installationName) {
		this.installationName = installationName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public List<BizReceiptAttachment> getFileList() {
		return fileList;
	}

	public void setFileList(List<BizReceiptAttachment> fileList) {
		this.fileList = fileList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public String getWriteOffRemark() {
		return writeOffRemark;
	}

	public void setWriteOffRemark(String writeOffRemark) {
		this.writeOffRemark = writeOffRemark;
	}

	public Integer getNewOtherInput() {
		return newOtherInput;
	}

	public void setNewOtherInput(Integer newOtherInput) {
		this.newOtherInput = newOtherInput;
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

	public Byte getRequestSource() {
		return requestSource;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getMoveTypeName() {
		return moveTypeName;
	}

	public void setMoveTypeName(String moveTypeName) {
		this.moveTypeName = moveTypeName;
	}

	public String getMoveTypeCode() {
		return moveTypeCode;
	}

	public void setMoveTypeCode(String moveTypeCode) {
		this.moveTypeCode = moveTypeCode;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getSpecStockCode() {
		return specStockCode;
	}

	public void setSpecStockCode(String specStockCode) {
		this.specStockCode = specStockCode;
	}

	public void setRequestSource(Byte requestSource) {
		this.requestSource = requestSource;
	}

	

	public List<BizStockInputItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizStockInputItem> itemList) {
		this.itemList = itemList;
	}



	private Integer stockInputId;

    private String stockInputCode;

    private Byte receiptType;

    private Byte status;

    private Date docTime;

    private String purchaseOrg;

    private String purchaseOrgName;

    private String purchaseGroup;

    private String purchaseGroupName;

    private Date postingDate;

    private String createUser;

    private Integer referDocId;

    private String remark;

    private String supplierCode;

    private String supplierName;

    private String exemptCheck;

    private String contractCode;

    private String purchaseOrderCode;

    private Integer corpId;

    private Integer locationId;

    private String purchaseOrderType;

    private String purchaseOrderTypeName;

    private Date submitTime;

    private Date inspectTime;

    private Integer moveTypeId;

    private Integer reasonId;

    private Integer ftyId;

    private Integer allocateId;

    private Integer allocateDeliveryId;

    private Byte checkAccount;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getStockInputId() {
        return stockInputId;
    }

    public void setStockInputId(Integer stockInputId) {
        this.stockInputId = stockInputId;
    }

    public String getStockInputCode() {
        return stockInputCode;
    }

    public void setStockInputCode(String stockInputCode) {
        this.stockInputCode = stockInputCode == null ? null : stockInputCode.trim();
    }

    public Byte getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Byte receiptType) {
        this.receiptType = receiptType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getDocTime() {
        return docTime;
    }

    public void setDocTime(Date docTime) {
        this.docTime = docTime;
    }

    public String getPurchaseOrg() {
        return purchaseOrg;
    }

    public void setPurchaseOrg(String purchaseOrg) {
        this.purchaseOrg = purchaseOrg == null ? null : purchaseOrg.trim();
    }

    public String getPurchaseOrgName() {
        return purchaseOrgName;
    }

    public void setPurchaseOrgName(String purchaseOrgName) {
        this.purchaseOrgName = purchaseOrgName == null ? null : purchaseOrgName.trim();
    }

    public String getPurchaseGroup() {
        return purchaseGroup;
    }

    public void setPurchaseGroup(String purchaseGroup) {
        this.purchaseGroup = purchaseGroup == null ? null : purchaseGroup.trim();
    }

    public String getPurchaseGroupName() {
        return purchaseGroupName;
    }

    public void setPurchaseGroupName(String purchaseGroupName) {
        this.purchaseGroupName = purchaseGroupName == null ? null : purchaseGroupName.trim();
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Integer getReferDocId() {
        return referDocId;
    }

    public void setReferDocId(Integer referDocId) {
        this.referDocId = referDocId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public String getExemptCheck() {
        return exemptCheck;
    }

    public void setExemptCheck(String exemptCheck) {
        this.exemptCheck = exemptCheck == null ? null : exemptCheck.trim();
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public String getPurchaseOrderCode() {
        return purchaseOrderCode;
    }

    public void setPurchaseOrderCode(String purchaseOrderCode) {
        this.purchaseOrderCode = purchaseOrderCode == null ? null : purchaseOrderCode.trim();
    }

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getPurchaseOrderType() {
        return purchaseOrderType;
    }

    public void setPurchaseOrderType(String purchaseOrderType) {
        this.purchaseOrderType = purchaseOrderType == null ? null : purchaseOrderType.trim();
    }

    public String getPurchaseOrderTypeName() {
        return purchaseOrderTypeName;
    }

    public void setPurchaseOrderTypeName(String purchaseOrderTypeName) {
        this.purchaseOrderTypeName = purchaseOrderTypeName == null ? null : purchaseOrderTypeName.trim();
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(Date inspectTime) {
        this.inspectTime = inspectTime;
    }

    public Integer getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(Integer moveTypeId) {
        this.moveTypeId = moveTypeId;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public Integer getAllocateId() {
        return allocateId;
    }

    public void setAllocateId(Integer allocateId) {
        this.allocateId = allocateId;
    }

    public Integer getAllocateDeliveryId() {
        return allocateDeliveryId;
    }

    public void setAllocateDeliveryId(Integer allocateDeliveryId) {
        this.allocateDeliveryId = allocateDeliveryId;
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

    private int totalCount = -1;
    
    @Override
	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getSysStatus() {
		return sysStatus;
	}

	public void setSysStatus(String sysStatus) {
		this.sysStatus = sysStatus;
	}

	public String getProductLineId() {
		return productLineId;
	}

	public void setProductLineId(String productLineId) {
		this.productLineId = productLineId;
	}

	public String getInstallationId() {
		return installationId;
	}

	public void setInstallationId(String installationId) {
		this.installationId = installationId;
	}
}
package com.inossem.wms.model.biz;

import java.util.Date;
import java.util.List;

import com.inossem.wms.model.auth.User;

public class BizStockOutputReturnHead {
	private String productLineName;
	private String installationName;
	private String classTypeName;
	
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
	
	
	private Integer productLineId;
	private Integer installationId;	
	
	public Integer getProductLineId() {
		return productLineId;
	}

	public void setProductLineId(Integer productLineId) {
		this.productLineId = productLineId;
	}

	public Integer getInstallationId() {
		return installationId;
	}

	public void setInstallationId(Integer installationId) {
		this.installationId = installationId;
	}

	private Integer classTypeId;
	
	private Byte synType;
	
	public Integer getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(Integer classTypeId) {
		this.classTypeId = classTypeId;
	}

	public Byte getSynType() {
		return synType;
	}

	public void setSynType(Byte synType) {
		this.synType = synType;
	}

	private List<BizStockOutputReturnItem> itemList;
	

	public List<BizStockOutputReturnItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizStockOutputReturnItem> itemList) {
		this.itemList = itemList;
	}

	//补充属性 SAP 过账
	private String voucherDate;
	private String postDate;
	private String currentUserId;
	private String werkToSap;

	
    public String getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getWerkToSap() {
		return werkToSap;
	}

	public void setWerkToSap(String werkToSap) {
		this.werkToSap = werkToSap;
	}

	private Integer returnId;

    private String returnCode;

    private Byte receiptType;

    private Integer stockOutputId;

    private String createUser;

    private String remark;

    private Byte status;

    private String referReceiptCode;

    private Integer ftyId;

    private String preorderId;

    private String customerCode;

    private String customerName;

    private String saleOrg;

    private String saleOrgName;

    private String saleGroup;

    private String saleGroupName;

    private String orderType;

    private String orderTypeName;
    
    private Byte requestSource;
    private String reserveCostObjCode;
    private String reserveCostObjName;
    private String reserveCreateUser;
    private Date reserveCreateTime;


    public Byte getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Byte requestSource) {
		this.requestSource = requestSource;
	}

	public String getReserveCostObjCode() {
		return reserveCostObjCode;
	}

	public void setReserveCostObjCode(String reserveCostObjCode) {
		this.reserveCostObjCode = reserveCostObjCode;
	}

	public String getReserveCostObjName() {
		return reserveCostObjName;
	}

	public void setReserveCostObjName(String reserveCostObjName) {
		this.reserveCostObjName = reserveCostObjName;
	}

	public String getReserveCreateUser() {
		return reserveCreateUser;
	}

	public void setReserveCreateUser(String reserveCreateUser) {
		this.reserveCreateUser = reserveCreateUser;
	}

	public Date getReserveCreateTime() {
		return reserveCreateTime;
	}

	public void setReserveCreateTime(Date reserveCreateTime) {
		this.reserveCreateTime = reserveCreateTime;
	}

	private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        this.returnId = returnId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode == null ? null : returnCode.trim();
    }

    public Byte getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Byte receiptType) {
        this.receiptType = receiptType;
    }

    public Integer getStockOutputId() {
        return stockOutputId;
    }

    public void setStockOutputId(Integer stockOutputId) {
        this.stockOutputId = stockOutputId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getStatus() {
        return status;
    }
    
    public String getStatusName() {
    	if (status == 0) {
			return "未退货";
		}else {
			return "已退货";
		}
     
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

    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public String getPreorderId() {
        return preorderId;
    }

    public void setPreorderId(String preorderId) {
        this.preorderId = preorderId == null ? null : preorderId.trim();
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode == null ? null : customerCode.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getSaleOrg() {
        return saleOrg;
    }

    public void setSaleOrg(String saleOrg) {
        this.saleOrg = saleOrg == null ? null : saleOrg.trim();
    }

    public String getSaleOrgName() {
        return saleOrgName;
    }

    public void setSaleOrgName(String saleOrgName) {
        this.saleOrgName = saleOrgName == null ? null : saleOrgName.trim();
    }

    public String getSaleGroup() {
        return saleGroup;
    }

    public void setSaleGroup(String saleGroup) {
        this.saleGroup = saleGroup == null ? null : saleGroup.trim();
    }

    public String getSaleGroupName() {
        return saleGroupName;
    }

    public void setSaleGroupName(String saleGroupName) {
        this.saleGroupName = saleGroupName == null ? null : saleGroupName.trim();
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
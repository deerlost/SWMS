package com.inossem.wms.model.biz;

import java.util.Date;

public class BizUrgenceHead {
    private Integer urgenceId;

    private String urgenceCode;

    private Byte receiptType;

    private Integer ftyId;

    private Integer locationId;

    private String demandDept;

    private String demandPerson;

    private String remark;

    private String createUser;

    private String modifyUser;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;
    
    private Byte status;

    public Integer getUrgenceId() {
        return urgenceId;
    }

    public void setUrgenceId(Integer urgenceId) {
        this.urgenceId = urgenceId;
    }

    public String getUrgenceCode() {
        return urgenceCode;
    }

    public void setUrgenceCode(String urgenceCode) {
        this.urgenceCode = urgenceCode == null ? null : urgenceCode.trim();
    }

    public Byte getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Byte receiptType) {
        this.receiptType = receiptType;
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

    public String getDemandDept() {
        return demandDept;
    }

    public void setDemandDept(String demandDept) {
        this.demandDept = demandDept == null ? null : demandDept.trim();
    }

    public String getDemandPerson() {
        return demandPerson;
    }

    public void setDemandPerson(String demandPerson) {
        this.demandPerson = demandPerson == null ? null : demandPerson.trim();
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

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
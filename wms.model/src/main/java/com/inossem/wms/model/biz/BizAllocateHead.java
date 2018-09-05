package com.inossem.wms.model.biz;

import java.util.Date;

public class BizAllocateHead {
    private Integer allocateId;

    private String allocateCode;

    private Integer corpId;

    private String orgId;

    private Integer ftyInput;

    private Integer locationInput;

    private Date demandDate;

    private Byte status;

    private Byte distributed;

    private String remark;

    private String createUser;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getAllocateId() {
        return allocateId;
    }

    public void setAllocateId(Integer allocateId) {
        this.allocateId = allocateId;
    }

    public String getAllocateCode() {
        return allocateCode;
    }

    public void setAllocateCode(String allocateCode) {
        this.allocateCode = allocateCode == null ? null : allocateCode.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public Integer getFtyInput() {
        return ftyInput;
    }

    public void setFtyInput(Integer ftyInput) {
        this.ftyInput = ftyInput;
    }

    public Integer getLocationInput() {
        return locationInput;
    }

    public void setLocationInput(Integer locationInput) {
        this.locationInput = locationInput;
    }

    public Date getDemandDate() {
        return demandDate;
    }

    public void setDemandDate(Date demandDate) {
        this.demandDate = demandDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getDistributed() {
        return distributed;
    }

    public void setDistributed(Byte distributed) {
        this.distributed = distributed;
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

	public Integer getCorpId() {
		return corpId;
	}

	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}
    
}
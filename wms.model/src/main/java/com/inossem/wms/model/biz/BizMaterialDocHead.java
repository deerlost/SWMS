package com.inossem.wms.model.biz;

import java.util.Date;
import java.util.List;

public class BizMaterialDocHead {
	
	//-----------start---
	
	private List<BizMaterialDocItem> materialDocItemList;
	
	//------------end---
	
    private Integer matDocId;

    public List<BizMaterialDocItem> getMaterialDocItemList() {
		return materialDocItemList;
	}

	public void setMaterialDocItemList(List<BizMaterialDocItem> materialDocItemList) {
		this.materialDocItemList = materialDocItemList;
	}

	private String matDocCode;

    private Byte matDocType;

    private Date docTime;

    private String orgCode;

    private String orgName;

    private String groupCode;

    private String groupName;

    private Date postingDate;

    private String remark;

    private String contractCode;

    private String useUnit;

    private String orderCode;

    private String orderType;

    private String orderTypeName;

    private String createUser;

    private Byte writeOff;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getMatDocId() {
        return matDocId;
    }

    public void setMatDocId(Integer matDocId) {
        this.matDocId = matDocId;
    }

    public String getMatDocCode() {
        return matDocCode;
    }

    public void setMatDocCode(String matDocCode) {
        this.matDocCode = matDocCode == null ? null : matDocCode.trim();
    }

    public Byte getMatDocType() {
        return matDocType;
    }

    public void setMatDocType(Byte matDocType) {
        this.matDocType = matDocType;
    }

    public Date getDocTime() {
        return docTime;
    }

    public void setDocTime(Date docTime) {
        this.docTime = docTime;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public String getUseUnit() {
        return useUnit;
    }

    public void setUseUnit(String useUnit) {
        this.useUnit = useUnit == null ? null : useUnit.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Byte getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(Byte writeOff) {
        this.writeOff = writeOff;
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
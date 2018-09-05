package com.inossem.wms.model.biz;

import java.util.Date;

import com.inossem.wms.model.enums.EnumMatReqStatus;

public class BizMatReqHead {
	private Integer matReqId;

	private String matReqCode;

	private Byte receiptType;

	private Integer applyFtyId;

	private Integer matReqFtyId;

	private Integer receiveFtyId;

	private Integer matReqMatTypeId;

	private Integer matReqBizTypeId;

	private String createUser;

	private String deptCode;

	private String deptName;

	private String internalOrderCode;

	private String internalOrderName;

	private String useDeptCode;

	private String useDeptName;

	private Byte isBuildingProject;

	private String remark;

	private String contractor;

	private Byte status;

	private String piid;

	private String project;

	private Byte isPortable;

	private Byte isDelete;

	private Date createTime;

	private Date modifyTime;

	public Integer getMatReqId() {
		return matReqId;
	}

	public void setMatReqId(Integer matReqId) {
		this.matReqId = matReqId;
	}

	public String getMatReqCode() {
		return matReqCode;
	}

	public void setMatReqCode(String matReqCode) {
		this.matReqCode = matReqCode == null ? null : matReqCode.trim();
	}

	public Byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Byte receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getApplyFtyId() {
		return applyFtyId;
	}

	public void setApplyFtyId(Integer applyFtyId) {
		this.applyFtyId = applyFtyId;
	}

	public Integer getMatReqFtyId() {
		return matReqFtyId;
	}

	public void setMatReqFtyId(Integer matReqFtyId) {
		this.matReqFtyId = matReqFtyId;
	}

	public Integer getReceiveFtyId() {
		return receiveFtyId;
	}

	public void setReceiveFtyId(Integer receiveFtyId) {
		this.receiveFtyId = receiveFtyId;
	}

	public Integer getMatReqMatTypeId() {
		return matReqMatTypeId;
	}

	public void setMatReqMatTypeId(Integer matReqMatTypeId) {
		this.matReqMatTypeId = matReqMatTypeId;
	}

	public Integer getMatReqBizTypeId() {
		return matReqBizTypeId;
	}

	public void setMatReqBizTypeId(Integer matReqBizTypeId) {
		this.matReqBizTypeId = matReqBizTypeId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode == null ? null : deptCode.trim();
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName == null ? null : deptName.trim();
	}

	public String getInternalOrderCode() {
		return internalOrderCode;
	}

	public void setInternalOrderCode(String internalOrderCode) {
		this.internalOrderCode = internalOrderCode == null ? null : internalOrderCode.trim();
	}

	public String getInternalOrderName() {
		return internalOrderName;
	}

	public void setInternalOrderName(String internalOrderName) {
		this.internalOrderName = internalOrderName == null ? null : internalOrderName.trim();
	}

	public String getUseDeptCode() {
		return useDeptCode;
	}

	public void setUseDeptCode(String useDeptCode) {
		this.useDeptCode = useDeptCode == null ? null : useDeptCode.trim();
	}

	public String getUseDeptName() {
		return useDeptName;
	}

	public void setUseDeptName(String useDeptName) {
		this.useDeptName = useDeptName == null ? null : useDeptName.trim();
	}

	public Byte getIsBuildingProject() {
		return isBuildingProject;
	}

	public void setIsBuildingProject(Byte isBuildingProject) {
		this.isBuildingProject = isBuildingProject;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getContractor() {
		return contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor == null ? null : contractor.trim();
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getStatusName() {
		return EnumMatReqStatus.getNameByValue(status);
	}

	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid == null ? null : piid.trim();
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project == null ? null : project.trim();
	}

	public Byte getIsPortable() {
		return isPortable;
	}

	public void setIsPortable(Byte isPortable) {
		this.isPortable = isPortable;
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
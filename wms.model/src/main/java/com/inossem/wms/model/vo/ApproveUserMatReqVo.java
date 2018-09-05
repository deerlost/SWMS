package com.inossem.wms.model.vo;

import java.util.Date;

import com.inossem.wms.model.enums.EnumApproveStatus;
import com.inossem.wms.model.page.IPageResultCommon;

public class ApproveUserMatReqVo implements IPageResultCommon {
	private String nodeName;
	private int receiptId;
	private String receiptCode;

	public String getReceiptCode() {
		return receiptCode;
	}

	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}

	private String corpName;
	private String orgName;
	private String userId;
	private String createUserId;
	private String createUserName;

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	private String piid;
	private byte receiptType;
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private int totalCount = -1;

	public byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(byte receiptType) {
		this.receiptType = receiptType;
	}

	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private String userName;
	private Byte approve;
	private Date approveTime;
	private String comment;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Byte getApprove() {
		return approve;
	}

	public void setApprove(Byte approve) {
		this.approve = approve;
	}

	public String getApproveName() {
		if (getApprove() == null) {
			return "";
		} else if (getApprove() == EnumApproveStatus.APPROVE_STATUS_ADOPT.getValue()) {
			return EnumApproveStatus.APPROVE_STATUS_ADOPT.getName();
		} else {
			return EnumApproveStatus.APPROVE_STATUS_REJECT.getName();
		}
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}

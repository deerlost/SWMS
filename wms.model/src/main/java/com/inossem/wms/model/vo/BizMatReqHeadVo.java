package com.inossem.wms.model.vo;

import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.page.IPageResultCommon;

public class BizMatReqHeadVo extends BizMatReqHead implements IPageResultCommon {
	private String matReqFtyCode;
	private String receiveFtyCode;
	private String applyFtyCode;

	private String matReqFtyName;
	private String receiveFtyName;
	private String applyFtyName;

	private String matTypeName;
	private String userName;
	private String bizTypeName;

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	private int boardId;

	private int totalCount = -1;

	public String getMatReqFtyCode() {
		return matReqFtyCode;
	}

	public void setMatReqFtyCode(String matReqFtyCode) {
		this.matReqFtyCode = matReqFtyCode;
	}

	public String getReceiveFtyCode() {
		return receiveFtyCode;
	}

	public void setReceiveFtyCode(String receiveFtyCode) {
		this.receiveFtyCode = receiveFtyCode;
	}

	public String getApplyFtyCode() {
		return applyFtyCode;
	}

	public void setApplyFtyCode(String applyFtyCode) {
		this.applyFtyCode = applyFtyCode;
	}

	public String getApplyFtyName() {
		return applyFtyName;
	}

	public void setApplyFtyName(String applyFtyName) {
		this.applyFtyName = applyFtyName;
	}

	public String getMatReqFtyName() {
		return matReqFtyName;
	}

	public void setMatReqFtyName(String matReqFtyName) {
		this.matReqFtyName = matReqFtyName;
	}

	public String getReceiveFtyName() {
		return receiveFtyName;
	}

	public void setReceiveFtyName(String receiveFtyName) {
		this.receiveFtyName = receiveFtyName;
	}

	public String getMatTypeName() {
		return matTypeName;
	}

	public void setMatTypeName(String matTypeName) {
		this.matTypeName = matTypeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

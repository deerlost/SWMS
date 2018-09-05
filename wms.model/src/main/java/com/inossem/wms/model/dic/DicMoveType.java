package com.inossem.wms.model.dic;

import java.util.List;

import com.inossem.wms.model.page.PageCommon;

public class DicMoveType extends PageCommon {

	List<DicMoveReason> reasonList;

	public List<DicMoveReason> getReasonList() {
		return reasonList;
	}

	public void setReasonList(List<DicMoveReason> reasonList) {
		this.reasonList = reasonList;
	}

	private Integer moveTypeId; // 移动类型表主键id

	private String moveTypeCode; // 移动类型

	private String specStock; // 特殊库存标识

	private String moveTypeName; // 移动类型描述

	private Byte bizType; // 业务类型

	private String bizTypeName;// 业务类型描述

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public Integer getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(Integer moveTypeId) {
		this.moveTypeId = moveTypeId;
	}

	public String getMoveTypeCode() {
		return moveTypeCode;
	}

	public void setMoveTypeCode(String moveTypeCode) {
		this.moveTypeCode = moveTypeCode;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock == null ? null : specStock.trim();
	}

	public String getMoveTypeName() {
		return moveTypeName;
	}

	public void setMoveTypeName(String moveTypeName) {
		this.moveTypeName = moveTypeName == null ? null : moveTypeName.trim();
	}

	public Byte getBizType() {
		return bizType;
	}

	public void setBizType(Byte bizType) {
		this.bizType = bizType;
	}
}
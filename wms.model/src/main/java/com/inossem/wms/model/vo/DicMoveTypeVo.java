package com.inossem.wms.model.vo;

import com.inossem.wms.model.dic.DicMoveType;

public class DicMoveTypeVo extends DicMoveType {

	private Integer reasonId;

	private String reasonCode;

	private String reasonName;

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
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

}

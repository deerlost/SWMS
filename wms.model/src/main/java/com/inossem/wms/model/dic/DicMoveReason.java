package com.inossem.wms.model.dic;

public class DicMoveReason {
	private Integer reasonId;

	private Integer moveTypeId;

	private String reasonCode;

	private String reasonName;

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(Integer moveTypeId) {
		this.moveTypeId = moveTypeId;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode == null ? null : reasonCode.trim();
	}

	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

}
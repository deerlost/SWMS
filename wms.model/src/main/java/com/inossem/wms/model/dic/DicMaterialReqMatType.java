package com.inossem.wms.model.dic;

public class DicMaterialReqMatType {
	private Integer matReqMatTypeId;

	private Integer receiptType;

	private Integer boardId;

	private String matTypeName;

	public Integer getMatReqMatTypeId() {
		return matReqMatTypeId;
	}

	public void setMatReqMatTypeId(Integer matReqMatTypeId) {
		this.matReqMatTypeId = matReqMatTypeId;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getBoardId() {
		return boardId;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}

	public String getMatTypeName() {
		return matTypeName;
	}

	public void setMatTypeName(String matTypeName) {
		this.matTypeName = matTypeName == null ? null : matTypeName.trim();
	}
}
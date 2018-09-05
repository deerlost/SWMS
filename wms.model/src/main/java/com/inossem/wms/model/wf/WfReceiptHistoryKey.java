package com.inossem.wms.model.wf;

public class WfReceiptHistoryKey {
	private Integer receiptId;

	private Byte receiptType;

	private String piid;

	public Integer getReceiptId() {
		return receiptId;
	}

	public Byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Byte receiptType) {
		this.receiptType = receiptType;
	}

	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

}
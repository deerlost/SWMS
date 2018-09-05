package com.inossem.wms.model.rel;

public class RelUserApprove {
	private Integer id;

	private String userId;

	private Integer receiptType;

	private String group;

	private String node;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group == null ? null : group.trim();
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node == null ? null : node.trim();
	}
}
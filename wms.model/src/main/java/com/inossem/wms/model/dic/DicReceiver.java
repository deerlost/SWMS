package com.inossem.wms.model.dic;

import com.inossem.wms.model.page.PageCommon;

public class DicReceiver extends PageCommon {
	private Integer id; // 接收方表主键

	private String receiveCode; // 接收方代码

	private String receiveName; // 接收方描述

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	private String condition; // 查询关键字

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode == null ? null : receiveCode.trim();
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName == null ? null : receiveName.trim();
	}
}
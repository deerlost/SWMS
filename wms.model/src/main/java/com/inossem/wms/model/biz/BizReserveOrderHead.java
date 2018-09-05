package com.inossem.wms.model.biz;

import java.util.List;

public class BizReserveOrderHead {

	/**
	 * SAP[RSNUM] 预留单号
	 */
	private String referReceiptCode;

	/**
	 * SAP[ZPOSID] 成本对象
	 */
	private String reserveCostObjCode;

	/**
	 * SAP[ZPOST1] 成本对象描述
	 */
	private String reserveCostObjName;

	/**
	 * SAP[USNAM] 创建人
	 */
	private String reserveCreateUser;

	/**
	 * SAP[RSDAT] 创建日期
	 */
	private String reserveCreateTime;

	//private Byte status;

	public String getReferReceiptCode() {
		return referReceiptCode;
	}

	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode;
	}

	public String getReserveCostObjCode() {
		return reserveCostObjCode;
	}

	public void setReserveCostObjCode(String reserveCostObjCode) {
		this.reserveCostObjCode = reserveCostObjCode;
	}

	public String getReserveCostObjName() {
		return reserveCostObjName;
	}

	public void setReserveCostObjName(String reserveCostObjName) {
		this.reserveCostObjName = reserveCostObjName;
	}

	public String getReserveCreateUser() {
		return reserveCreateUser;
	}

	public void setReserveCreateUser(String reserveCreateUser) {
		this.reserveCreateUser = reserveCreateUser;
	}

	public String getReserveCreateTime() {
		return reserveCreateTime;
	}

	public void setReserveCreateTime(String reserveCreateTime) {
		this.reserveCreateTime = reserveCreateTime;
	}

//	public Byte getStatus() {
//		return status;
//	}
//
//	public void setStatus(Byte status) {
//		this.status = status;
//	}
}

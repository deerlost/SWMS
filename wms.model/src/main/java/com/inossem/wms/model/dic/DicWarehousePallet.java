package com.inossem.wms.model.dic;

import java.math.BigDecimal;
import java.util.Date;

import com.inossem.wms.model.page.PageCommon;

public class DicWarehousePallet extends PageCommon {
	private Integer palletId; // 托盘表主键

	private String palletCode; // 托盘号

	private String palletName; // 托盘类型

	private String whId; // 仓库号

	private BigDecimal maxWeight; // 最大承重

	private String unitWeight; // 最大承重单位

	private Byte freeze; // 冻结标示 0 非冻结 1冻结

	private Byte freezeId; // 默认0

	private Byte status; // 空托盘标识,0-空

	private Byte isDelete; // 是否删除

	private Date createTime; // 创建时间

	private Date modifyTime; // 修改时间

	private String condition; // 关键字查询
	
	private String createUser;
	
	private String createUserName;

	
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getPalletCode1() {
		return palletCode1;
	}

	public void setPalletCode1(String palletCode1) {
		this.palletCode1 = palletCode1;
	}

	public String getPalletCode2() {
		return palletCode2;
	}

	public void setPalletCode2(String palletCode2) {
		this.palletCode2 = palletCode2;
	}

	public String getLgtyp() {
		return lgtyp;
	}

	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}

	private String palletCode1; // 批量添加托盘时的起始托盘号

	private String palletCode2; // 批量添加托盘时的结束托盘号

	public String getWh_name() {
		return wh_name;
	}

	public void setWh_name(String wh_name) {
		this.wh_name = wh_name;
	}

	private String wh_name; // 仓库号描述

	private String lgtyp; // 打印时的托盘id

	public Integer getPalletId() {
		return palletId;
	}

	public void setPalletId(Integer palletId) {
		this.palletId = palletId;
	}

	public String getPalletCode() {
		return palletCode;
	}

	public void setPalletCode(String palletCode) {
		this.palletCode = palletCode == null ? null : palletCode.trim();
	}

	public String getPalletName() {
		return palletName;
	}

	public void setPalletName(String palletName) {
		this.palletName = palletName == null ? null : palletName.trim();
	}

	public String getWhId() {
		return whId;
	}

	public void setWhId(String whId) {
		this.whId = whId == null ? null : whId.trim();
	}

	public BigDecimal getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(BigDecimal maxWeight) {
		this.maxWeight = maxWeight;
	}

	public String getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(String unitWeight) {
		this.unitWeight = unitWeight == null ? null : unitWeight.trim();
	}

	public Byte getFreeze() {
		return freeze;
	}

	public void setFreeze(Byte freeze) {
		this.freeze = freeze;
	}

	public Byte getFreezeId() {
		return freezeId;
	}

	public void setFreezeId(Byte freezeId) {
		this.freezeId = freezeId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
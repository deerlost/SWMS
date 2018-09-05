package com.inossem.wms.model.dic;

import java.util.Date;

public class DicWarehouseArea {
	private Integer areaId;

	private Integer whId;

	private String whCode;

	private String areaCode;

	private String areaName;

	private Byte checkMethod;

	private Byte mix;

	private Byte typeInput;

	private Byte typeOutput;

	private Byte template;

	private Byte isDelete;

	private Date createTime;

	private Date modifyTime;

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getWhId() {
		return whId;
	}

	public void setWhId(Integer whId) {
		this.whId = whId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode == null ? null : areaCode.trim();
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName == null ? null : areaName.trim();
	}

	public Byte getCheckMethod() {
		return checkMethod;
	}

	public void setCheckMethod(Byte checkMethod) {
		this.checkMethod = checkMethod;
	}

	public Byte getMix() {
		return mix;
	}

	public void setMix(Byte mix) {
		this.mix = mix;
	}

	public Byte getTypeInput() {
		return typeInput;
	}

	public void setTypeInput(Byte typeInput) {
		this.typeInput = typeInput;
	}

	public Byte getTypeOutput() {
		return typeOutput;
	}

	public void setTypeOutput(Byte typeOutput) {
		this.typeOutput = typeOutput;
	}

	public Byte getTemplate() {
		return template;
	}

	public void setTemplate(Byte template) {
		this.template = template;
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

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
}
package com.inossem.wms.model.vo;

import java.io.Serializable;

public class MobileStocktakeAreaPositionVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String itemId;                // 序列号
	private String positionId;			  // 仓位ID
	private String suffix;         		  // 仓位号, 显示内容：来自盘点明细suffix
	private String positionCode;   		  // 仓位号
	private String type;                  // 类型 0已有，1新增
	private String status;                // 状态 0未盘点，1部分盘点，2全部物料盘点完成（新增物料只有2这个状态）
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getPositionCode() {
		return positionCode;
	}
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}

package com.inossem.wms.model.key;

public class DicWarehousePositionKey {
	private int whId;
	private int areaId;
	private String positionCode;
	private int positionId;
	
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public int getWhId() {
		return whId;
	}
	public void setWhId(int whId) {
		this.whId = whId;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public String getPositionCode() {
		return positionCode;
	}
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
}

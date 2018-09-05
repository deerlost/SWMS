package com.inossem.wms.model.vo;

import com.inossem.wms.model.rel.RelUserStockLocation;

public class RelUserStockLocationVo extends RelUserStockLocation {

	private String ftyCode;
	private String ftyName;
	private String ftyType;
	private String locationCode;
	private String locationName;

	
	
	public String getFtyType() {
		return ftyType;
	}

	public void setFtyType(String ftyType) {
		this.ftyType = ftyType;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getWhName() {
		return whName;
	}

	public void setWhName(String whName) {
		this.whName = whName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private int whId;
	private String whCode;
	private String whName;
	private String address;

	public int getWhId() {
		return whId;
	}

	public void setWhId(int whId) {
		this.whId = whId;
	}

	private Byte status;
	private Byte is_delete;
	private Byte freezeOutput;
	private Byte freezeInput;
	private int boardId;

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(Byte is_delete) {
		this.is_delete = is_delete;
	}

	public Byte getFreezeOutput() {
		return freezeOutput;
	}

	public void setFreezeOutput(Byte freezeOutput) {
		this.freezeOutput = freezeOutput;
	}

	public Byte getFreezeInput() {
		return freezeInput;
	}

	public void setFreezeInput(Byte freezeInput) {
		this.freezeInput = freezeInput;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getFtyCode() {
		return ftyCode;
	}

	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}

	public String getFtyName() {
		return ftyName;
	}

	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

}

package com.inossem.wms.model.vo;

import java.math.BigDecimal;

import com.inossem.wms.model.biz.BizMatReqItem;

public class BizMatReqItemVo extends BizMatReqItem {
	private String matCode;
	private String matName;
	private String moveTypeCode;
	private String unitCode;
	private String unitName;
	private String locationCode;
	private String locationName;

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

	private BigDecimal currentQty;
	private String moveTypeName;
	private String specStock;

	private byte decimalPlace;

	public byte getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	public String getMoveTypeName() {
		return moveTypeName;
	}

	public void setMoveTypeName(String moveTypeName) {
		this.moveTypeName = moveTypeName;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
	}

	public BigDecimal getCurrentQty() {
		return currentQty;
	}

	public void setCurrentQty(BigDecimal currentQty) {
		this.currentQty = currentQty;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getMatName() {
		return matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getMoveTypeCode() {
		return moveTypeCode;
	}

	public void setMoveTypeCode(String moveTypeCode) {
		this.moveTypeCode = moveTypeCode;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
}

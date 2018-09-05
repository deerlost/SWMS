package com.inossem.wms.model.vo;

import java.math.BigDecimal;

import com.inossem.wms.model.page.IPageResultCommon;
import com.inossem.wms.model.stock.StockBatch;

public class StockBatchVo extends StockBatch implements IPageResultCommon {
	private String matCode;
	private String matName;
	private int unitId;
	private String unitCode;
	private String locationCode;
	private String locationName;
	private BigDecimal decimalPrice;
	private String matGroupName;
	private String isFocusBatch;
	private int totalCount;
	private int ftyId;
	
	
	
	public String getIsFocusBatch() {
		return isFocusBatch;
	}

	public void setIsFocusBatch(String isFocusBatch) {
		this.isFocusBatch = isFocusBatch;
	}

	public BigDecimal getDecimalPrice() {
		return decimalPrice;
	}

	public void setDecimalPrice(BigDecimal decimalPrice) {
		this.decimalPrice = decimalPrice;
	}

	public String getMatGroupName() {
		return matGroupName;
	}

	public void setMatGroupName(String matGroupName) {
		this.matGroupName = matGroupName;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

	public int getAssetAttr() {
		return assetAttr;
	}

	public void setAssetAttr(int assetAttr) {
		this.assetAttr = assetAttr;
	}

	private int assetAttr;

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public String getMatName() {
		return matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
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

	public BigDecimal getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(BigDecimal decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	private String unitName;
	private BigDecimal decimalPlace;



	public int getFtyId() {
		return ftyId;
	}

	public void setFtyId(int ftyId) {
		this.ftyId = ftyId;
	}

	
}

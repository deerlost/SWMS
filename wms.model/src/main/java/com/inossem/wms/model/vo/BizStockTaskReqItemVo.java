package com.inossem.wms.model.vo;

import java.math.BigDecimal;

import com.inossem.wms.model.biz.BizStockTaskReqItem;

public class BizStockTaskReqItemVo extends BizStockTaskReqItem {
	
	private BigDecimal unStockTaskQty;
	
	private BigDecimal packageStandard;
	private String stockTaskReqCode;
	private String packageTypeName;
	private String packageTypeCode;
	
	public BigDecimal getPackageStandard() {
		return packageStandard;
	}
	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}
	public BigDecimal getUnStockTaskQty() {
		return unStockTaskQty;
	}
	public void setUnStockTaskQty(BigDecimal unStockTaskQty) {
		this.unStockTaskQty = unStockTaskQty;
	}
	public String getStockTaskReqCode() {
		return stockTaskReqCode;
	}
	public void setStockTaskReqCode(String stockTaskReqCode) {
		this.stockTaskReqCode = stockTaskReqCode;
	}
	public String getPackageTypeName() {
		return packageTypeName;
	}
	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName;
	}
	public String getPackageTypeCode() {
		return packageTypeCode;
	}
	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
	}
	private String matCode;
	private String matName;
	private int decimalPlace;
	private String locationCode;
	private String locationName;
	
	private String nameZh;
	
	public String getNameZh() {
		return nameZh;
	}
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}
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
	public int getDecimalPlace() {
		return decimalPlace;
	}
	public void setDecimalPlace(int decimalPlace) {
		this.decimalPlace = decimalPlace;
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

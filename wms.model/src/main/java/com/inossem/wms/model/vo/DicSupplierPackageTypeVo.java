package com.inossem.wms.model.vo;

import com.inossem.wms.model.dic.DicSupplierPackageType;

public class DicSupplierPackageTypeVo extends DicSupplierPackageType{

	private String unitCode;
	private String unitName;
	private String classificatName;
	private String cityName;
	private String supplierCode;
	private String supplierName;
	private String  nameZh;
	private String packageTypeCode;	
	private String packageTypeName;
	
	public String getPackageTypeCode() {
		return packageTypeCode;
	}
	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
	}
	public String getPackageTypeName() {
		return packageTypeName;
	}
	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName;
	}
	public String getNameZh() {
		return nameZh;
	}
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
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
	public String getClassificatName() {
		return classificatName;
	}
	public void setClassificatName(String classificatName) {
		this.classificatName = classificatName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}



}
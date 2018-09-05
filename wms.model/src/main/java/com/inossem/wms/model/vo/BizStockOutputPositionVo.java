package com.inossem.wms.model.vo;



import java.math.BigDecimal;

import com.inossem.wms.model.biz.BizStockOutputPosition;

public class BizStockOutputPositionVo extends BizStockOutputPosition {

	private String packageTypeCode;
	
	private String packageTypeName;

	private String locationCode;

	private String locationName;
	
	private String mesLocationCode;

	private String ftyCode;

	private String ftyName;

	private String matCode;
	
	private String matName;
	
	private String unitCode;

	private String unitEn;

	private String unitZh;

	private String packageStandardCh;

	private BigDecimal packageStandard;

	private String stockTypeName;
	
	private BigDecimal outputQty;
	
	
	public String getMesLocationCode() {
		return mesLocationCode;
	}

	public void setMesLocationCode(String mesLocationCode) {
		this.mesLocationCode = mesLocationCode;
	}

	public BigDecimal getOutputQty() {
		return outputQty;
	}

	public void setOutputQty(BigDecimal outputQty) {
		this.outputQty = outputQty;
	}

	public String getPackageStandardCh() {
		return packageStandardCh;
	}

	public void setPackageStandardCh(String packageStandardCh) {
		this.packageStandardCh = packageStandardCh;
	}

	public BigDecimal getPackageStandard() {
		return packageStandard;
	}

	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}

	public String getStockTypeName() {
		return stockTypeName;
	}

	public void setStockTypeName(String stockTypeName) {
		this.stockTypeName = stockTypeName;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitEn() {
		return unitEn;
	}

	public void setUnitEn(String unitEn) {
		this.unitEn = unitEn;
	}

	public String getUnitZh() {
		return unitZh;
	}

	public void setUnitZh(String unitZh) {
		this.unitZh = unitZh;
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

	public String getFtyCode() {
		return ftyCode;
	}

	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}

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

	
}

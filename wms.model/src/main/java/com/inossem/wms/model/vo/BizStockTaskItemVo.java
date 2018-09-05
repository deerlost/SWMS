package com.inossem.wms.model.vo;

import java.math.BigDecimal;

import com.inossem.wms.model.biz.BizStockTaskItemCw;

public class BizStockTaskItemVo extends BizStockTaskItemCw{
	
	private BigDecimal packageStandard;
	
	private String stockTaskReqCode;
	
	private String packageTypeCode;
	
	private String packageTypeName;
	
	
	public BigDecimal getPackageStandard() {
		return packageStandard;
	}

	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}

	public String getStockTaskReqCode() {
		return stockTaskReqCode;
	}

	public void setStockTaskReqCode(String stockTaskReqCode) {
		this.stockTaskReqCode = stockTaskReqCode;
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

	private BigDecimal unstockTaskQty;
	
	private String locationCode;
	
	private String locationName;
	
	
	public BigDecimal getUnstockTaskQty() {
		return unstockTaskQty;
	}

	public void setUnstockTaskQty(BigDecimal unstockTaskQty) {
		this.unstockTaskQty = unstockTaskQty;
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

	private String nameZh;
	
	
	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	private String matCode;
	
	
	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	private BigDecimal batchqty;
	
	private String matName;

	public BigDecimal getBatchqty() {
		return batchqty;
	}

	public void setBatchqty(BigDecimal batchqty) {
		this.batchqty = batchqty;
	}

	public String getMatName() {
		return matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}
	
	

}

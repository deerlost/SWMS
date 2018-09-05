package com.inossem.wms.model.vo;

import com.inossem.wms.model.dic.DicPackageType;

public class DicPackageTypeVo extends DicPackageType {
	
    private String unitCode;
    
    private String unitName;
    
    //包id
    private Integer packageId;
    //包code
    private String packageCode;
    
	private Integer matUnitId;
	private String matUnitCode;
	private String matNameZh;
	
    
	public Integer getMatUnitId() {
		return matUnitId;
	}

	public void setMatUnitId(Integer matUnitId) {
		this.matUnitId = matUnitId;
	}

	public String getMatUnitCode() {
		return matUnitCode;
	}

	public void setMatUnitCode(String matUnitCode) {
		this.matUnitCode = matUnitCode;
	}

	public String getMatNameZh() {
		return matNameZh;
	}

	public void setMatNameZh(String matNameZh) {
		this.matNameZh = matNameZh;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getUnitCode() {
        return this.unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}

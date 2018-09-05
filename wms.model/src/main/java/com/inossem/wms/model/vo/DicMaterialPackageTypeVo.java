package com.inossem.wms.model.vo;

import com.inossem.wms.model.dic.DicMaterialPackageType;

public class DicMaterialPackageTypeVo extends DicMaterialPackageType  {
	private String unitCode;
	private String nameZh;
	private String classificatId;
	private String packageTypeCode;
	private String packageTypeName;
	private String classificatName;
	private Integer matUnitId;
	private String matUnitCode;
	private String matNameZh;
	private String matType;
	private String matName;
	private String matCode;
    private String snUsedName;
    private String erpBatchKey;
    private String  snSerialKey;

	public String getErpBatchKey() {
		return erpBatchKey;
	}

	public void setErpBatchKey(String erpBatchKey) {
		this.erpBatchKey = erpBatchKey;
	}

	public String getSnSerialKey() {
		return snSerialKey;
	}

	public void setSnSerialKey(String snSerialKey) {
		this.snSerialKey = snSerialKey;
	}

	public String getSnUsedName() {
		return snUsedName;
	}

	public void setSnUsedName(String snUsedName) {
		this.snUsedName = snUsedName;
	}

	public String getMatName() {
		return matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public String getMatType() {
		return matType;
	}

	public void setMatType(String matType) {
		this.matType = matType;
	}

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

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	public String getClassificatId() {
		return classificatId;
	}

	public void setClassificatId(String classificatId) {
		this.classificatId = classificatId;
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

	public String getClassificatName() {
		return classificatName;
	}

	public void setClassificatName(String classificatName) {
		this.classificatName = classificatName;
	}



	
}

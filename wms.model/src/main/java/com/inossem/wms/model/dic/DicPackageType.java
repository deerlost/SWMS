package com.inossem.wms.model.dic;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.page.PageCommon;

public class DicPackageType extends PageCommon{
	
	private List<Map<String, Object>> erpBatchList;
	
    public List<Map<String, Object>> getErpBatchList() {
		return erpBatchList;
	}

	public void setErpBatchList(List<Map<String, Object>> erpBatchList) {
		this.erpBatchList = erpBatchList;
	}

	private Integer packageTypeId;
	private String packageTypeCode;
	private String packageTypeName;
	private String packageStandardCh;
	private BigDecimal packageStandard;
	private Integer packageGroupId;
	private Integer unitId;
	private Byte isDelete;
	private Byte isFreeze;
	private Byte snUsed;
	private BigDecimal size;
	private Integer classificatId;
	private Integer matUnitId;
	private String erpBatchKey;
	private String snSerialKey;
	private List<DicSupplier> supplierList;
	public Integer getMatUnitId() {
		return matUnitId;
	}

	public void setMatUnitId(Integer matUnitId) {
		this.matUnitId = matUnitId;
	}

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


	
	public List<DicSupplier> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List<DicSupplier> supplierList) {
		this.supplierList = supplierList;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public String getPackageTypeCode() {
		return packageTypeCode;
	}

	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode == null ? null : packageTypeCode.trim();
	}

	public String getPackageTypeName() {
		return packageTypeName;
	}

	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName == null ? null : packageTypeName.trim();
	}

	public String getPackageStandardCh() {
		return packageStandardCh;
	}

	public void setPackageStandardCh(String packageStandardCh) {
		this.packageStandardCh = packageStandardCh == null ? null : packageStandardCh.trim();
	}

	public BigDecimal getPackageStandard() {
		return packageStandard;
	}

	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}

	public Integer getPackageGroupId() {
		return packageGroupId;
	}

	public void setPackageGroupId(Integer packageGroupId) {
		this.packageGroupId = packageGroupId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	public Byte getIsFreeze() {
		return isFreeze;
	}

	public void setIsFreeze(Byte isFreeze) {
		this.isFreeze = isFreeze;
	}

	public Byte getSnUsed() {
		return snUsed;
	}

	public void setSnUsed(Byte snUsed) {
		this.snUsed = snUsed;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}


	public Integer getClassificatId() {
		return classificatId;
	}

	public void setClassificatId(Integer classificatId) {
		this.classificatId = classificatId;
	}

	
}

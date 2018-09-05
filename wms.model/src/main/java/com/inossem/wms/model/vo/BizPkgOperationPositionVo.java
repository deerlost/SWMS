package com.inossem.wms.model.vo;

import java.math.BigDecimal;

public class BizPkgOperationPositionVo{

	//---------
	private String unitZh;
	private String unitEn;
	//---------
	
    private Integer itemPositionId;

	private Integer pkgOperationId;
    private Integer pkgOperationRid;
    private Integer pkgOperationPositionId;
    //单位
    private Integer unitId;
    private String unitCode;
    private String unitName;
    //托盘
    private Integer palletId;
    private String palletCode;
    private String palletName;
    private BigDecimal maxWeight;
    //包
    private Integer packageId;
    private String packageCode;
    //包类型
    private Integer packageTypeId;
    private String packageTypeCode;
    private String packageTypeName;
    private String packageStandardCh;
    private BigDecimal packageStandard;
    private Integer packageGroupId;
    private String size;
    private Byte snUsed;

    
    
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitZh() {
		return unitZh;
	}

	public void setUnitZh(String unitZh) {
		this.unitZh = unitZh;
	}

	public String getUnitEn() {
		return unitEn;
	}

	public void setUnitEn(String unitEn) {
		this.unitEn = unitEn;
	}

	public String getPalletName() {
		return palletName;
	}

	public void setPalletName(String palletName) {
		this.palletName = palletName;
	}

	public BigDecimal getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(BigDecimal maxWeight) {
		this.maxWeight = maxWeight;
	}

	public String getPackageTypeCode() {
		return packageTypeCode;
	}

	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Byte getSnUsed() {
		return snUsed;
	}

	public void setSnUsed(Byte snUsed) {
		this.snUsed = snUsed;
	}

	public Integer getItemPositionId() {
		return itemPositionId;
	}

	public void setItemPositionId(Integer itemPositionId) {
		this.itemPositionId = itemPositionId;
	}

	public Integer getPkgOperationId() {
		return pkgOperationId;
	}

	public void setPkgOperationId(Integer pkgOperationId) {
		this.pkgOperationId = pkgOperationId;
	}

	public Integer getPkgOperationRid() {
		return pkgOperationRid;
	}

	public void setPkgOperationRid(Integer pkgOperationRid) {
		this.pkgOperationRid = pkgOperationRid;
	}

	public Integer getPkgOperationPositionId() {
		return pkgOperationPositionId;
	}

	public void setPkgOperationPositionId(Integer pkgOperationPositionId) {
		this.pkgOperationPositionId = pkgOperationPositionId;
	}

	public Integer getPalletId() {
		return palletId;
	}

	public void setPalletId(Integer palletId) {
		this.palletId = palletId;
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

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public BizPkgOperationPositionVo() {
    }

    public String getPackageTypeName() {
        return this.packageTypeName;
    }

    public void setPackageTypeName(String packageTypeName) {
        this.packageTypeName = packageTypeName;
    }

    public String getUnitCode() {
        return this.unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getPalletCode() {
        return this.palletCode;
    }

    public void setPalletCode(String palletCode) {
        this.palletCode = palletCode;
    }
}

package com.inossem.wms.model.vo;
import com.inossem.wms.model.vo.BizPkgOperationPositionVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BizPkgOperationItemVo{
	
    private List<BizPkgOperationPositionVo> positionList;
    private List<Map<String,Object>> packageTypeList;
    private String matCode;
    private String matName;
    private Integer unitId;
    private String unitCode;
    private String unitName;
    private String nameEn;
    private String nameZh;
    private Integer itemId;
    private Integer pkgOperationId;
    private Integer pkgOperationRid;
    private Integer ftyId;
    private Integer matId;
    private BigDecimal orderQty;
    private BigDecimal pkgQty;
    private Integer packageTypeId;
    private String packageTypeCode;
    private String packageTypeName;
    private BigDecimal packageStandard;
    private String packageStandardCh;

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

	public BigDecimal getPackageStandard() {
		return packageStandard;
	}

	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}

	public String getPackageStandardCh() {
		return packageStandardCh;
	}

	public void setPackageStandardCh(String packageStandardCh) {
		this.packageStandardCh = packageStandardCh;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public List<Map<String, Object>> getPackageTypeList() {
		return packageTypeList;
	}

	public void setPackageTypeList(List<Map<String, Object>> packageTypeList) {
		this.packageTypeList = packageTypeList;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public Integer getFtyId() {
		return ftyId;
	}

	public void setFtyId(Integer ftyId) {
		this.ftyId = ftyId;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}

	public BigDecimal getPkgQty() {
		return pkgQty;
	}

	public void setPkgQty(BigDecimal pkgQty) {
		this.pkgQty = pkgQty;
	}

	public List<BizPkgOperationPositionVo> getPositionList() {
        return this.positionList;
    }

    public void setPositionList(List<BizPkgOperationPositionVo> positionList) {
        this.positionList = positionList;
    }

    public String getMatName() {
        return this.matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getUnitCode() {
        return this.unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}

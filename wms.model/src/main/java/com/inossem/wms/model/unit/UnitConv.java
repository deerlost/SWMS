package com.inossem.wms.model.unit;

import java.math.BigDecimal;
import java.util.Date;

public class UnitConv {
    private Integer unitConvId;

    private Integer matGroupId;

    private Integer matId;

    private Integer unitId;

    private String constName;

    private BigDecimal constValue;

    private Date createTime;

    private Date modifyTime;
    
    private String matGroupCode;
    
    private String matCode;
    
    private String matName;
    
    private String unitCode;

    public Integer getUnitConvId() {
        return unitConvId;
    }

    public void setUnitConvId(Integer unitConvId) {
        this.unitConvId = unitConvId;
    }

    public Integer getMatGroupId() {
        return matGroupId;
    }

    public void setMatGroupId(Integer matGroupId) {
        this.matGroupId = matGroupId;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName = constName == null ? null : constName.trim();
    }

    public BigDecimal getConstValue() {
        return constValue;
    }

    public void setConstValue(BigDecimal constValue) {
        this.constValue = constValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

	public String getMatGroupCode() {
		return matGroupCode;
	}

	public void setMatGroupCode(String matGroupCode) {
		this.matGroupCode = matGroupCode;
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

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
    
    
    
}
package com.inossem.wms.model.dic;

import java.math.BigDecimal;
import java.util.List;

public class DicMaterial {
	
	
	private String sampName;
	
	private String specification;
	
	private String standard;

	//---------------
	

	
	/**
	 * 批次特性
	 */
	private List<DicBatchSpec> batchSpecList;
	
	/**
	 * 批次信息
	 */
	private List<DicBatchSpec> batchMaterialSpecList;
	
	private String condition;
	
	private String unitCode;
	
	private String nameZh;
	
	private Byte decimalPlace;
	
	private String matGroupName;
	
	private String matGroupCode;
	
	//---------------
	
	
    public List<DicBatchSpec> getBatchSpecList() {
		return batchSpecList;
	}

	public String getSampName() {
		return sampName;
	}

	public void setSampName(String sampName) {
		this.sampName = sampName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public void setBatchSpecList(List<DicBatchSpec> batchSpecList) {
		this.batchSpecList = batchSpecList;
	}

	public List<DicBatchSpec> getBatchMaterialSpecList() {
		return batchMaterialSpecList;
	}

	public void setBatchMaterialSpecList(List<DicBatchSpec> batchMaterialSpecList) {
		this.batchMaterialSpecList = batchMaterialSpecList;
	}

	private Integer matId;

    public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
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

	public Byte getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(Byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	public String getMatGroupName() {
		return matGroupName;
	}

	public void setMatGroupName(String matGroupName) {
		this.matGroupName = matGroupName;
	}

	public String getMatGroupCode() {
		return matGroupCode;
	}

	public void setMatGroupCode(String matGroupCode) {
		this.matGroupCode = matGroupCode;
	}

	private String matCode;

    private String matName;

    private Integer unitId;

    private Integer matGroupId;

    private String matType;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private Integer unitHeight;

    private BigDecimal grossWeight;

    private BigDecimal netWeight;

    private Integer unitWeight;

    private BigDecimal volume;

    private Integer unitVolume;

    private Integer shelfLife;

    private Byte isDanger;

    private Integer batchSpecClassifyId;

    private Byte assetAttr;

    private Byte costCheck;

    private Byte isDelete;

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode == null ? null : matCode.trim();
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName == null ? null : matName.trim();
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getMatGroupId() {
        return matGroupId;
    }

    public void setMatGroupId(Integer matGroupId) {
        this.matGroupId = matGroupId;
    }

    public String getMatType() {
        return matType;
    }

    public void setMatType(String matType) {
        this.matType = matType == null ? null : matType.trim();
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Integer getUnitHeight() {
        return unitHeight;
    }

    public void setUnitHeight(Integer unitHeight) {
        this.unitHeight = unitHeight;
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public BigDecimal getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
    }

    public Integer getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Integer unitWeight) {
        this.unitWeight = unitWeight;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public Integer getUnitVolume() {
        return unitVolume;
    }

    public void setUnitVolume(Integer unitVolume) {
        this.unitVolume = unitVolume;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Byte getIsDanger() {
        return isDanger;
    }

    public void setIsDanger(Byte isDanger) {
        this.isDanger = isDanger;
    }

    public Integer getBatchSpecClassifyId() {
        return batchSpecClassifyId;
    }

    public void setBatchSpecClassifyId(Integer batchSpecClassifyId) {
        this.batchSpecClassifyId = batchSpecClassifyId;
    }

    public Byte getAssetAttr() {
        return assetAttr;
    }

    public void setAssetAttr(Byte assetAttr) {
        this.assetAttr = assetAttr;
    }

    public Byte getCostCheck() {
        return costCheck;
    }

    public void setCostCheck(Byte costCheck) {
        this.costCheck = costCheck;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
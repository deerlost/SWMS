package com.inossem.wms.model.dic;

import java.util.Date;

public class DicWarehousePosition {
	
	private Byte isDefault;

    public Byte getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}

	private Integer positionId;

    private Integer whId;

    private Integer areaId;

    private String positionCode;

    private String positionIndex1;

    private String positionIndex2;

    private String positionIndex3;

    private Byte freezeStocktake;

    private Byte freezeInput;

    private Byte freezeOutput;

    private Byte freezeId;

    private Integer unitWeight;

    private Integer unitVolume;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;
    
    private String whCode;
    
    private String areaCode;
    
    private String whName;
    
    private String areaName;
    
    private Integer storageTypeId;
    
    private String storageTypeCode;
    
    private String storageTypeName;
    
    

    public Integer getStorageTypeId() {
		return storageTypeId;
	}

	public void setStorageTypeId(Integer storageTypeId) {
		this.storageTypeId = storageTypeId;
	}

	public String getStorageTypeCode() {
		return storageTypeCode;
	}

	public void setStorageTypeCode(String storageTypeCode) {
		this.storageTypeCode = storageTypeCode;
	}

	public String getStorageTypeName() {
		return storageTypeName;
	}

	public void setStorageTypeName(String storageTypeName) {
		this.storageTypeName = storageTypeName;
	}

	public String getWhName() {
		return whName;
	}

	public void setWhName(String whName) {
		this.whName = whName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode == null ? null : positionCode.trim();
    }

    public String getPositionIndex1() {
        return positionIndex1;
    }

    public void setPositionIndex1(String positionIndex1) {
        this.positionIndex1 = positionIndex1 == null ? null : positionIndex1.trim();
    }

    public String getPositionIndex2() {
        return positionIndex2;
    }

    public void setPositionIndex2(String positionIndex2) {
        this.positionIndex2 = positionIndex2 == null ? null : positionIndex2.trim();
    }

    public String getPositionIndex3() {
        return positionIndex3;
    }

    public void setPositionIndex3(String positionIndex3) {
        this.positionIndex3 = positionIndex3 == null ? null : positionIndex3.trim();
    }

    public Byte getFreezeStocktake() {
        return freezeStocktake;
    }

    public void setFreezeStocktake(Byte freezeStocktake) {
        this.freezeStocktake = freezeStocktake;
    }

    public Byte getFreezeInput() {
        return freezeInput;
    }

    public void setFreezeInput(Byte freezeInput) {
        this.freezeInput = freezeInput;
    }

    public Byte getFreezeOutput() {
        return freezeOutput;
    }

    public void setFreezeOutput(Byte freezeOutput) {
        this.freezeOutput = freezeOutput;
    }

    public Byte getFreezeId() {
        return freezeId;
    }

    public void setFreezeId(Byte freezeId) {
        this.freezeId = freezeId;
    }

    public Integer getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Integer unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Integer getUnitVolume() {
        return unitVolume;
    }

    public void setUnitVolume(Integer unitVolume) {
        this.unitVolume = unitVolume;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
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

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
    
    
}
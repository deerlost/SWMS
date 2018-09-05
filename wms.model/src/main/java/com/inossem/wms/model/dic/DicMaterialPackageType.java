package com.inossem.wms.model.dic;

import java.math.BigDecimal;

import com.inossem.wms.model.page.PageCommon;

public class DicMaterialPackageType extends PageCommon {
    private Integer dicMatPackageTypeId;

    private Integer matId;

    private Integer packageTypeId;

    private Byte isFreeze;

    private BigDecimal conversRelation;
    
    private Integer snUsed;

    

    public Integer getDicMatPackageTypeId() {
        return dicMatPackageTypeId;
    }

    public void setDicMatPackageTypeId(Integer dicMatPackageTypeId) {
        this.dicMatPackageTypeId = dicMatPackageTypeId;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public Byte getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(Byte isFreeze) {
        this.isFreeze = isFreeze;
    }

    public BigDecimal getConversRelation() {
        return conversRelation;
    }

    public void setConversRelation(BigDecimal conversRelation) {
        this.conversRelation = conversRelation;
    }

	public Integer getSnUsed() {
		return snUsed;
	}

	public void setSnUsed(Integer snUsed) {
		this.snUsed = snUsed;
	}
}
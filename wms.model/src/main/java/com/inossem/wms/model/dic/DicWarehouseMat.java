package com.inossem.wms.model.dic;

import java.util.Date;

public class DicWarehouseMat {
    private Integer whMatId;

    private Integer matId;

    private Integer whId;

    private Byte stockListingType;

    private Byte stockRemovalType;

    private Integer unitId;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getWhMatId() {
        return whMatId;
    }

    public void setWhMatId(Integer whMatId) {
        this.whMatId = whMatId;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Byte getStockListingType() {
        return stockListingType;
    }

    public void setStockListingType(Byte stockListingType) {
        this.stockListingType = stockListingType;
    }

    public Byte getStockRemovalType() {
        return stockRemovalType;
    }

    public void setStockRemovalType(Byte stockRemovalType) {
        this.stockRemovalType = stockRemovalType;
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
}
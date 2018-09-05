package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizAllocateDeliveryItem {
    private Integer itemId;

    private Integer allocateDeliveryId;

    private Integer allocateDeliveryRid;

    private Integer allocateItemId;

    private Integer matId;

    private Integer unitId;

    private Integer ftyInput;

    private Integer locationInput;

    private BigDecimal allocateQty;

    private Byte status;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAllocateDeliveryId() {
        return allocateDeliveryId;
    }

    public void setAllocateDeliveryId(Integer allocateDeliveryId) {
        this.allocateDeliveryId = allocateDeliveryId;
    }

    public Integer getAllocateDeliveryRid() {
        return allocateDeliveryRid;
    }

    public void setAllocateDeliveryRid(Integer allocateDeliveryRid) {
        this.allocateDeliveryRid = allocateDeliveryRid;
    }

    public Integer getAllocateItemId() {
        return allocateItemId;
    }

    public void setAllocateItemId(Integer allocateItemId) {
        this.allocateItemId = allocateItemId;
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

    public Integer getFtyInput() {
        return ftyInput;
    }

    public void setFtyInput(Integer ftyInput) {
        this.ftyInput = ftyInput;
    }

    public Integer getLocationInput() {
        return locationInput;
    }

    public void setLocationInput(Integer locationInput) {
        this.locationInput = locationInput;
    }

    public BigDecimal getAllocateQty() {
        return allocateQty;
    }

    public void setAllocateQty(BigDecimal allocateQty) {
        this.allocateQty = allocateQty;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
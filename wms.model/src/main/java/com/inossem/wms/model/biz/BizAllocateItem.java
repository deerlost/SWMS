package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizAllocateItem {
    private Integer itemId;

    private Integer allocateId;

    private Integer allocateRid;

    private Integer deliveryItemId;

    private Integer matId;

    private Integer unitId;

    private Integer ftyOutput;

    private Integer locationOutput;

    private BigDecimal stockQty;

    private BigDecimal allocateQty;

    private Byte status;

    private Integer stockOutputId;

    private Integer stockOutputRid;

    private Integer stockInputId;

    private Integer stockInputRid;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAllocateId() {
        return allocateId;
    }

    public void setAllocateId(Integer allocateId) {
        this.allocateId = allocateId;
    }

    public Integer getAllocateRid() {
        return allocateRid;
    }

    public void setAllocateRid(Integer allocateRid) {
        this.allocateRid = allocateRid;
    }

    public Integer getDeliveryItemId() {
        return deliveryItemId;
    }

    public void setDeliveryItemId(Integer deliveryItemId) {
        this.deliveryItemId = deliveryItemId;
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

    public Integer getFtyOutput() {
        return ftyOutput;
    }

    public void setFtyOutput(Integer ftyOutput) {
        this.ftyOutput = ftyOutput;
    }

    public Integer getLocationOutput() {
        return locationOutput;
    }

    public void setLocationOutput(Integer locationOutput) {
        this.locationOutput = locationOutput;
    }

    public BigDecimal getStockQty() {
        return stockQty;
    }

    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
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

    public Integer getStockOutputId() {
        return stockOutputId;
    }

    public void setStockOutputId(Integer stockOutputId) {
        this.stockOutputId = stockOutputId;
    }

    public Integer getStockOutputRid() {
        return stockOutputRid;
    }

    public void setStockOutputRid(Integer stockOutputRid) {
        this.stockOutputRid = stockOutputRid;
    }

    public Integer getStockInputId() {
        return stockInputId;
    }

    public void setStockInputId(Integer stockInputId) {
        this.stockInputId = stockInputId;
    }

    public Integer getStockInputRid() {
        return stockInputRid;
    }

    public void setStockInputRid(Integer stockInputRid) {
        this.stockInputRid = stockInputRid;
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
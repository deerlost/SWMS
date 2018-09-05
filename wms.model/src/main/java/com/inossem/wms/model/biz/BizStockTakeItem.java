package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockTakeItem {
    private Integer itemId;

    private Integer stockTakeId;

    private Integer stockTakeRid;

    private Integer whId;

    private Integer areaId;

    private Integer positionId;

    private Integer matId;

    private Integer unitId;

    private Long batch;

    private Date inputTime;

    private BigDecimal stockQty;

    private BigDecimal qty;

    private Date stockTakeTime;

    private String stockTakeUser;

    private String remark;

    private Byte requestSource;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getStockTakeId() {
        return stockTakeId;
    }

    public void setStockTakeId(Integer stockTakeId) {
        this.stockTakeId = stockTakeId;
    }

    public Integer getStockTakeRid() {
        return stockTakeRid;
    }

    public void setStockTakeRid(Integer stockTakeRid) {
        this.stockTakeRid = stockTakeRid;
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

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
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

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public BigDecimal getStockQty() {
        return stockQty;
    }

    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Date getStockTakeTime() {
        return stockTakeTime;
    }

    public void setStockTakeTime(Date stockTakeTime) {
        this.stockTakeTime = stockTakeTime;
    }

    public String getStockTakeUser() {
        return stockTakeUser;
    }

    public void setStockTakeUser(String stockTakeUser) {
        this.stockTakeUser = stockTakeUser == null ? null : stockTakeUser.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(Byte requestSource) {
        this.requestSource = requestSource;
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
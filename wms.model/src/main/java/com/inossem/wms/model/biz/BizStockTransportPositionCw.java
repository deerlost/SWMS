package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockTransportPositionCw {
    private Integer itemPositionId;

    private Integer stockTransportId;

    private Integer stockTransportRid;

    private Integer stockId;

    private Integer stockSnId;

    private Long batch;

    private Integer snId;

    private Integer palletId;

    private Integer packageId;

    private BigDecimal qty;

    private Integer whId;

    private String remark;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    private String createUser;

    public Integer getItemPositionId() {
        return itemPositionId;
    }

    public void setItemPositionId(Integer itemPositionId) {
        this.itemPositionId = itemPositionId;
    }

    public Integer getStockTransportId() {
        return stockTransportId;
    }

    public void setStockTransportId(Integer stockTransportId) {
        this.stockTransportId = stockTransportId;
    }

    public Integer getStockTransportRid() {
        return stockTransportRid;
    }

    public void setStockTransportRid(Integer stockTransportRid) {
        this.stockTransportRid = stockTransportRid;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Integer getStockSnId() {
        return stockSnId;
    }

    public void setStockSnId(Integer stockSnId) {
        this.stockSnId = stockSnId;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public Integer getSnId() {
        return snId;
    }

    public void setSnId(Integer snId) {
        this.snId = snId;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }
}
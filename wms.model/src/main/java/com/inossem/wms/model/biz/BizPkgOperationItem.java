package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizPkgOperationItem {
    private Integer itemId;
    private Integer pkgOperationId;
    private Integer pkgOperationRid;
    private Integer ftyId;
    private Integer locationId;
    private Integer matId;
    private Long batch;
    private Byte isDelete;
    private Date createTime;
    private Date modifyTime;
    private BigDecimal orderQty;
    private BigDecimal pkgQty;
    private Integer packageTypeId;
    

    public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Integer getItemId() {
        return this.itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPkgOperationId() {
        return this.pkgOperationId;
    }

    public void setPkgOperationId(Integer pkgOperationId) {
        this.pkgOperationId = pkgOperationId;
    }

    public Integer getPkgOperationRid() {
        return this.pkgOperationRid;
    }

    public void setPkgOperationRid(Integer pkgOperationRid) {
        this.pkgOperationRid = pkgOperationRid;
    }

    public Integer getFtyId() {
        return this.ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public Integer getLocationId() {
        return this.locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getMatId() {
        return this.matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Long getBatch() {
        return this.batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public Byte getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public BigDecimal getOrderQty() {
        return this.orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getPkgQty() {
        return this.pkgQty;
    }

    public void setPkgQty(BigDecimal pkgQty) {
        this.pkgQty = pkgQty;
    }
}

package com.inossem.wms.model.stock;

import java.math.BigDecimal;
import java.util.Date;

public class StockBatchBegin {
    private Integer id;

    private Integer matId;

    private Integer locationId;

    private BigDecimal qty;

    private Byte stockTypeId;

    private Date createTime;

    private Byte status;
    
    private String erpBatch;

    public String getErpBatch() {
		return erpBatch;
	}

	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
    
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Byte getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(Byte stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
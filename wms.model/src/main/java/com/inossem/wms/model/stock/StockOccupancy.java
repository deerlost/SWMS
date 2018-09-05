package com.inossem.wms.model.stock;

import java.math.BigDecimal;

public class StockOccupancy {
	
	private Byte isUrgent; 
	
    private Byte stockTypeId;

    private Integer matId;

    private Integer ftyId;

    private Integer locationId;

    private Integer packageTypeId;

    private Long batch;

    private BigDecimal qty;

    public Byte getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(Byte isUrgent) {
		this.isUrgent = isUrgent;
	}

	public Byte getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(Byte stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
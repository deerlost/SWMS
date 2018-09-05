package com.inossem.wms.model.key;

public class StockPositionKey {
	
	private Byte urgentSynStatus;
	
	public Byte getUrgentSynStatus() {
		return urgentSynStatus;
	}

	public void setUrgentSynStatus(Byte urgentSynStatus) {
		this.urgentSynStatus = urgentSynStatus;
	}

	private Byte stockTypeId;
	
	private Byte isUrgent;

	private Integer positionId;

	private Integer palletId;

	private Integer packageId;

	private Integer matId;

	private Integer locationId;

	private Long batch;

	private Byte status;
	
	public Byte getStockTypeId() {
		return stockTypeId;
	}

	public void setStockTypeId(Byte stockTypeId) {
		this.stockTypeId = stockTypeId;
	}

	public Byte getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(Byte isUrgent) {
		this.isUrgent = isUrgent;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
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

	public Long getBatch() {
		return batch;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}

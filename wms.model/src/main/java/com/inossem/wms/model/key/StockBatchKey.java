package com.inossem.wms.model.key;

public class StockBatchKey {
	
	private Byte urgentSynStatus;
	
	private Byte isUrgent;
	private Byte stockTypeId;
	private int matId;
	private int locationId;
	private long batch;
	private Byte status;

	public Byte getUrgentSynStatus() {
		return urgentSynStatus;
	}

	public void setUrgentSynStatus(Byte urgentSynStatus) {
		this.urgentSynStatus = urgentSynStatus;
	}

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

	public int getMatId() {
		return matId;
	}

	public void setMatId(int matId) {
		this.matId = matId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public long getBatch() {
		return batch;
	}

	public void setBatch(long batch) {
		this.batch = batch;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}

package com.inossem.wms.model.key;

public class BatchMaterialSpecKey {
	private int matId;
	private int ftyId;
	private long batch;

	public int getMatId() {
		return matId;
	}

	public void setMatId(int matId) {
		this.matId = matId;
	}

	public int getFtyId() {
		return ftyId;
	}

	public void setFtyId(int ftyId) {
		this.ftyId = ftyId;
	}

	public long getBatch() {
		return batch;
	}

	public void setBatch(long batch) {
		this.batch = batch;
	}

	
}

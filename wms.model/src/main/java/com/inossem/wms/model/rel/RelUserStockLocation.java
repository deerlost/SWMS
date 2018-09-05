package com.inossem.wms.model.rel;

public class RelUserStockLocation {
	private Integer id;

	private String userId;

	private Integer ftyId;

	private Integer locationId;

	private Byte locationIndex;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
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

	public Byte getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(Byte locationIndex) {
		this.locationIndex = locationIndex;
	}
}
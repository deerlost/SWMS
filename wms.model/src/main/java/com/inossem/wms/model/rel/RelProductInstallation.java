package com.inossem.wms.model.rel;

public class RelProductInstallation {
    private Integer id;

	private Integer productLineId;

	private Integer installationId;

	private Byte isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductLineId() {
		return productLineId;
	}

	public void setProductLineId(Integer productLineId) {
		this.productLineId = productLineId;
	}

	public Integer getInstallationId() {
		return installationId;
	}

	public void setInstallationId(Integer installationId) {
		this.installationId = installationId;
	}

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	
}
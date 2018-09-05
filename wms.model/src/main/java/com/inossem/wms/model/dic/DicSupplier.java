package com.inossem.wms.model.dic;

public class DicSupplier {
    private Integer supplierId;

    private String supplierCode;

    private String supplierName;

    private Byte idDelete;

    private Integer cityId;
    
    private String cityName;
    
    public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public Byte getIdDelete() {
        return idDelete;
    }

    public void setIdDelete(Byte idDelete) {
        this.idDelete = idDelete;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
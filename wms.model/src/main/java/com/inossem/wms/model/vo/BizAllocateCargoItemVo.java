package com.inossem.wms.model.vo;


import java.math.BigDecimal;

public class BizAllocateCargoItemVo{
	private Integer allocateCargoId;
	private Integer allocateCargoRid;
    private Integer ftyId;
    private String ftyCode;
    private String ftyName;
    private Integer locationId;
    private String locationCode;
    private String locationName;
    private Integer matId;
    private String matCode;
    private String matName;
    private Integer unitId;
    private String unitCode;
    private String unitName;
    private BigDecimal orderQty;
    private BigDecimal outputQty;
    private String erpBatch;
    private String erpRemark;
    private String referReceiptRid;
    
    public Integer getAllocateCargoId() {
		return allocateCargoId;
	}

	public void setAllocateCargoId(Integer allocateCargoId) {
		this.allocateCargoId = allocateCargoId;
	}

	public Integer getAllocateCargoRid() {
		return allocateCargoRid;
	}

	public void setAllocateCargoRid(Integer allocateCargoRid) {
		this.allocateCargoRid = allocateCargoRid;
	}

	public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public String getErpBatch() {
        return erpBatch;
    }

    public void setErpBatch(String erpBatch) {
        this.erpBatch = erpBatch;
    }

    public String getErpRemark() {
        return erpRemark;
    }

    public void setErpRemark(String erpRemark) {
        this.erpRemark = erpRemark;
    }

    public String getReferReceiptRid() {
        return referReceiptRid;
    }

    public void setReferReceiptRid(String referReceiptRid) {
        this.referReceiptRid = referReceiptRid;
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

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getOutputQty() {
        return outputQty;
    }

    public void setOutputQty(BigDecimal outputQty) {
        this.outputQty = outputQty;
    }

    public String getFtyCode() {
        return ftyCode;
    }

    public void setFtyCode(String ftyCode) {
        this.ftyCode = ftyCode;
    }

    public String getFtyName() {
        return ftyName;
    }

    public void setFtyName(String ftyName) {
        this.ftyName = ftyName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
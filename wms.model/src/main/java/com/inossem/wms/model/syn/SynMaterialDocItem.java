package com.inossem.wms.model.syn;

import java.math.BigDecimal;
import java.util.Date;

public class SynMaterialDocItem {
    private Integer itemId;

    private Integer matDocId;

    private Integer matDocRid;

    private Long batch;

    private Integer moveTypeId;

    private Integer matId;

    private String matCode;

    private String matName;

    private String stockStatus;

    private String specStock;
    
    private String specStockCode;

    private String specStockName;

    private BigDecimal qty;

    private Integer unitId;

    private Integer ftyId;

    private Integer locationId;

    private String reserveId;

    private String reserveRid;

    private String createUser;

    private String supplierCode;

    private String supplierName;

    private String customerCode;

    private String customerName;

    private String saleOrderCode;

    private String saleOrderPrjectCode;

    private String saleOrderDeliveredPlan;

    private String debitCredit;

    private Byte isNew;

    private Byte isNeutralize;

    private Date createTime;

    private Date modifyTime;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getMatDocId() {
        return matDocId;
    }

    public void setMatDocId(Integer matDocId) {
        this.matDocId = matDocId;
    }

    public Integer getMatDocRid() {
        return matDocRid;
    }

    public void setMatDocRid(Integer matDocRid) {
        this.matDocRid = matDocRid;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public Integer getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(Integer moveTypeId) {
        this.moveTypeId = moveTypeId;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode == null ? null : matCode.trim();
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName == null ? null : matName.trim();
    }

    public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getSpecStockCode() {
        return specStockCode;
    }

    public void setSpecStockCode(String specStockCode) {
        this.specStockCode = specStockCode == null ? null : specStockCode.trim();
    }

    public String getSpecStockName() {
        return specStockName;
    }

    public void setSpecStockName(String specStockName) {
        this.specStockName = specStockName == null ? null : specStockName.trim();
    }

    public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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

    public String getReserveId() {
        return reserveId;
    }

    public void setReserveId(String reserveId) {
        this.reserveId = reserveId == null ? null : reserveId.trim();
    }

    public String getReserveRid() {
        return reserveRid;
    }

    public void setReserveRid(String reserveRid) {
        this.reserveRid = reserveRid == null ? null : reserveRid.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode == null ? null : customerCode.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getSaleOrderCode() {
        return saleOrderCode;
    }

    public void setSaleOrderCode(String saleOrderCode) {
        this.saleOrderCode = saleOrderCode == null ? null : saleOrderCode.trim();
    }

    public String getSaleOrderPrjectCode() {
        return saleOrderPrjectCode;
    }

    public void setSaleOrderPrjectCode(String saleOrderPrjectCode) {
        this.saleOrderPrjectCode = saleOrderPrjectCode == null ? null : saleOrderPrjectCode.trim();
    }

    public String getSaleOrderDeliveredPlan() {
        return saleOrderDeliveredPlan;
    }

    public void setSaleOrderDeliveredPlan(String saleOrderDeliveredPlan) {
        this.saleOrderDeliveredPlan = saleOrderDeliveredPlan == null ? null : saleOrderDeliveredPlan.trim();
    }

    public String getDebitCredit() {
        return debitCredit;
    }

    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit == null ? null : debitCredit.trim();
    }

    public Byte getIsNew() {
        return isNew;
    }

    public void setIsNew(Byte isNew) {
        this.isNew = isNew;
    }

    public Byte getIsNeutralize() {
        return isNeutralize;
    }

    public void setIsNeutralize(Byte isNeutralize) {
        this.isNeutralize = isNeutralize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
	}
    
    
}
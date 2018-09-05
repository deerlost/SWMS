package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockOutputPosition {
    private Integer itemPositionId;

    private Integer stockOutputId;

    private Integer stockOutputRid;

    private Integer stockOutputPositionId;

    private Integer stockId;

    private Integer palletId;

    private Integer packageId;

    private BigDecimal qty;

    private BigDecimal returnQty;

    private Integer whId;

    private Integer areaId;

    private Integer positionId;

    private BigDecimal stockQty;

    private String remark;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    private String createUser;

    private String modifyUser;

    private Integer stockSnId;

    private Integer snId;

    private Integer matId;
    
    private String specStock;

    private String specStockCode;

    private String specStockName;

    private Long batch;

    private String erpBatch;

    private String productBatch;

    private String qualityBatch;

    private Integer locationId;

    private Integer ftyId;

    private Integer packageTypeId;

    private String inputDate;

    private Integer unitId;

    private Byte stockTypeId;
    
    private String matDocCode;
    
    private String mesDocCode;
    
    public String getMesDocCode() {
		return mesDocCode;
	}

	public void setMesDocCode(String mesDocCode) {
		this.mesDocCode = mesDocCode;
	}

	public String getMatDocCode() {
		return matDocCode;
	}

	public void setMatDocCode(String matDocCode) {
		this.matDocCode = matDocCode;
	}

	public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Byte getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(Byte stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }

    public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
	}

	public Integer getItemPositionId() {
        return itemPositionId;
    }

    public void setItemPositionId(Integer itemPositionId) {
        this.itemPositionId = itemPositionId;
    }

    public Integer getStockOutputId() {
        return stockOutputId;
    }

    public void setStockOutputId(Integer stockOutputId) {
        this.stockOutputId = stockOutputId;
    }

    public Integer getStockOutputRid() {
        return stockOutputRid;
    }

    public void setStockOutputRid(Integer stockOutputRid) {
        this.stockOutputRid = stockOutputRid;
    }

    public Integer getStockOutputPositionId() {
        return stockOutputPositionId;
    }

    public void setStockOutputPositionId(Integer stockOutputPositionId) {
        this.stockOutputPositionId = stockOutputPositionId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public BigDecimal getStockQty() {
        return stockQty;
    }

    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }

    public Integer getStockSnId() {
        return stockSnId;
    }

    public void setStockSnId(Integer stockSnId) {
        this.stockSnId = stockSnId;
    }

    public Integer getSnId() {
        return snId;
    }

    public void setSnId(Integer snId) {
        this.snId = snId;
    }

    public String getSpecStock() {
        return specStock;
    }

    public void setSpecStock(String specStock) {
        this.specStock = specStock == null ? null : specStock.trim();
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

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public String getErpBatch() {
        return erpBatch;
    }

    public void setErpBatch(String erpBatch) {
        this.erpBatch = erpBatch == null ? null : erpBatch.trim();
    }

    public String getProductBatch() {
        return productBatch;
    }

    public void setProductBatch(String productBatch) {
        this.productBatch = productBatch == null ? null : productBatch.trim();
    }

    public String getQualityBatch() {
        return qualityBatch;
    }

    public void setQualityBatch(String qualityBatch) {
        this.qualityBatch = qualityBatch == null ? null : qualityBatch.trim();
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }
}
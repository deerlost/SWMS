package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.inossem.wms.model.dic.DicBatchSpec;

public class BizStockInputItem {
	
	// ----------补充属性start---------
	
	private List<BizStockInputPackageItem> packageItemList;
	
	private Long batchOld;
	
	private String matDocCode;
	
	private String matOffCode;
	
	private Integer matDocItemId;
	
	private String nameZh;
	
	private String matCode;
	private String matName;
	
	private Integer allocateItemId;
	
	private String locationCode;
	
	private String locationName;
	
	private String ftyCode;
	
	private String ftyName;
	
	private String matDocYear;
	
	
	private String mesDocCode;
	
	private String mesLocationId;
	
	private String mesFailCode;
	
	/**
	 * 批次特性
	 */
	private List<DicBatchSpec> batchSpecList;
	
	/**
	 * 批次信息
	 */
	private List<DicBatchSpec> batchMaterialSpecList;
	/**
	 * 特殊库存标识
	 */
	private String specStock;

	// ----------补充属性end---------
	
    public String getLocationCode() {
		return locationCode;
	}

	public List<BizStockInputPackageItem> getPackageItemList() {
		return packageItemList;
	}

	public void setPackageItemList(List<BizStockInputPackageItem> packageItemList) {
		this.packageItemList = packageItemList;
	}

	public Long getBatchOld() {
		return batchOld;
	}

	public void setBatchOld(Long batchOld) {
		this.batchOld = batchOld;
	}

	public String getMatDocCode() {
		return matDocCode;
	}

	public void setMatDocCode(String matDocCode) {
		this.matDocCode = matDocCode;
	}

	public Integer getMatDocItemId() {
		return matDocItemId;
	}

	public void setMatDocItemId(Integer matDocItemId) {
		this.matDocItemId = matDocItemId;
	}

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
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

	public Integer getAllocateItemId() {
		return allocateItemId;
	}

	public void setAllocateItemId(Integer allocateItemId) {
		this.allocateItemId = allocateItemId;
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

	public List<DicBatchSpec> getBatchSpecList() {
		return batchSpecList;
	}

	public List<DicBatchSpec> getBatchMaterialSpecList() {
		return batchMaterialSpecList;
	}

	public void setBatchMaterialSpecList(List<DicBatchSpec> batchMaterialSpecList) {
		this.batchMaterialSpecList = batchMaterialSpecList;
	}

	public void setBatchSpecList(List<DicBatchSpec> batchSpecList) {
		this.batchSpecList = batchSpecList;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
	}

	private Integer itemId;

    private Integer stockInputId;

    private Integer stockInputRid;

    private Integer moveTypeId;

    private Integer moveReasonId;

    private Integer matId;

    private Integer ftyId;

    private Integer locationId;

    private Long batch;

    private String specStockCode;

    private String specStockName;

    private String supplierCode;

    private String supplierName;

    private String customerCode;

    private String customerName;

    private String demandDept;

    private String saleOrderCode;

    private String saleOrderPrjectCode;

    private String saleOrderDeliveredPlan;

    private BigDecimal standardCurrencyMoney;

    private BigDecimal orderQty;

    private BigDecimal qty;

    private Integer unitId;

    private String purchaseOrderCode;

    private String purchaseOrderRid;

    private String contractCode;

    private String contractName;

    private String referReceiptCode;

    private String referReceiptRid;

    private Integer matDocId;

    private Integer matDocRid;

    private String remark;

    private String costObjCode;

    private String costObjName;

    private String costObjLevel;

    private String reserveId;

    private String reserveRid;

    private BigDecimal inputedQty;

    private String manufacturer;

    private Byte decimalPlace;

    private Byte status;

    private Byte writeOff;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getStockInputId() {
        return stockInputId;
    }

    public void setStockInputId(Integer stockInputId) {
        this.stockInputId = stockInputId;
    }

    public Integer getStockInputRid() {
        return stockInputRid;
    }

    public void setStockInputRid(Integer stockInputRid) {
        this.stockInputRid = stockInputRid;
    }

    public Integer getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(Integer moveTypeId) {
        this.moveTypeId = moveTypeId;
    }

    public Integer getMoveReasonId() {
        return moveReasonId;
    }

    public void setMoveReasonId(Integer moveReasonId) {
        this.moveReasonId = moveReasonId;
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

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
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

    public String getDemandDept() {
        return demandDept;
    }

    public void setDemandDept(String demandDept) {
        this.demandDept = demandDept == null ? null : demandDept.trim();
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

    public BigDecimal getStandardCurrencyMoney() {
        return standardCurrencyMoney;
    }

    public void setStandardCurrencyMoney(BigDecimal standardCurrencyMoney) {
        this.standardCurrencyMoney = standardCurrencyMoney;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
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

    public String getPurchaseOrderCode() {
        return purchaseOrderCode;
    }

    public void setPurchaseOrderCode(String purchaseOrderCode) {
        this.purchaseOrderCode = purchaseOrderCode == null ? null : purchaseOrderCode.trim();
    }

    public String getPurchaseOrderRid() {
        return purchaseOrderRid;
    }

    public void setPurchaseOrderRid(String purchaseOrderRid) {
        this.purchaseOrderRid = purchaseOrderRid == null ? null : purchaseOrderRid.trim();
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public String getReferReceiptCode() {
        return referReceiptCode;
    }

    public void setReferReceiptCode(String referReceiptCode) {
        this.referReceiptCode = referReceiptCode == null ? null : referReceiptCode.trim();
    }

    public String getReferReceiptRid() {
        return referReceiptRid;
    }

    public void setReferReceiptRid(String referReceiptRid) {
        this.referReceiptRid = referReceiptRid == null ? null : referReceiptRid.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCostObjCode() {
        return costObjCode;
    }

    public void setCostObjCode(String costObjCode) {
        this.costObjCode = costObjCode == null ? null : costObjCode.trim();
    }

    public String getCostObjName() {
        return costObjName;
    }

    public void setCostObjName(String costObjName) {
        this.costObjName = costObjName == null ? null : costObjName.trim();
    }

    public String getCostObjLevel() {
        return costObjLevel;
    }

    public void setCostObjLevel(String costObjLevel) {
        this.costObjLevel = costObjLevel == null ? null : costObjLevel.trim();
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

    public BigDecimal getInputedQty() {
        return inputedQty;
    }

    public void setInputedQty(BigDecimal inputedQty) {
        this.inputedQty = inputedQty;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public Byte getDecimalPlace() {
        return decimalPlace;
    }

    public void setDecimalPlace(Byte decimalPlace) {
        this.decimalPlace = decimalPlace;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(Byte writeOff) {
        this.writeOff = writeOff;
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

	public String getMacDocYear() {
		return matDocYear;
	}

	public void setMacDocYear(String matDocYear) {
		this.matDocYear = matDocYear;
	}

	public String getMesDocCode() {
		return mesDocCode;
	}

	public void setMesDocCode(String mesDocCode) {
		this.mesDocCode = mesDocCode;
	}

	public String getMesLocationId() {
		return mesLocationId;
	}

	public void setMesLocationId(String mesLocationId) {
		this.mesLocationId = mesLocationId;
	}

	public String getMesFailCode() {
		return mesFailCode;
	}

	public void setMesFailCode(String mesFailCode) {
		this.mesFailCode = mesFailCode;
	}

	public String getMatOffCode() {
		return matOffCode;
	}

	public void setMatOffCode(String matOffCode) {
		this.matOffCode = matOffCode;
	}
}
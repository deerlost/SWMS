package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.inossem.wms.model.dic.DicBatchSpec;

public class BizMaterialDocItem {
	
	//------补充是新建批次还是获取的最近批次
	
	boolean isRecentBatch ;

	public boolean isRecentBatch() {
		return isRecentBatch;
	}

	public void setRecentBatch(boolean isRecentBatch) {
		this.isRecentBatch = isRecentBatch;
	}
	
	private Long referBatch;
	
	

	// -----------------
	
	public Long getReferBatch() {
		return referBatch;
	}

	public void setReferBatch(Long referBatch) {
		this.referBatch = referBatch;
	}

	private Integer referReceiptItemId;
	
	/**
	 * 批次特性
	 */
	private List<DicBatchSpec> batchSpecList;

	/**
	 * 批次信息
	 */
	private List<DicBatchSpec> batchMaterialSpecList;

	// -----------------

	private String moveTypeCode;

	public Integer getReferReceiptItemId() {
		return referReceiptItemId;
	}

	public void setReferReceiptItemId(Integer referReceiptItemId) {
		this.referReceiptItemId = referReceiptItemId;
	}

	public List<DicBatchSpec> getBatchSpecList() {
		return batchSpecList;
	}

	public void setBatchSpecList(List<DicBatchSpec> batchSpecList) {
		this.batchSpecList = batchSpecList;
	}

	public List<DicBatchSpec> getBatchMaterialSpecList() {
		return batchMaterialSpecList;
	}

	public void setBatchMaterialSpecList(List<DicBatchSpec> batchMaterialSpecList) {
		this.batchMaterialSpecList = batchMaterialSpecList;
	}

	private Integer itemId;

	private Integer matDocId;

	private Integer matDocRid;

	private Integer matDocYear;

	private Long batch;

	private Integer moveTypeId;

	private Integer matId;

	private Integer ftyId;

	private Integer locationId;

	private String specStock;

	private String specStockCode;

	private String specStockName;

	private String supplierCode;

	private String supplierName;

	private String customerCode;

	private String customerName;

	private String demandDept;

	private String saleOrderCode;

	private String saleOrderProjectCode;

	private String saleOrderDeliveredPlan;

	private BigDecimal standardCurrencyMoney;

	private BigDecimal orderQty;

	private BigDecimal qty;

	private Integer unitId;

	private String purchaseOrderCode;

	private String purchaseOrderRid;

	private String contractCode;

	private String contractName;

	private Integer referReceiptId;

	private String referReceiptCode;

	private Integer referReceiptRid;

	private Integer referMatDocId;

	private Integer referMatDocRid;

	private String remark;

	private String costObjCode;

	private String costObjName;

	private String costObjLevel;

	private String reserveId;

	private String reserveRid;

	private Byte decimalPlace;

	private String debitCredit;

	private Byte writeOff;

	private Byte status;

	private Byte isDelete;

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

	public Integer getMatDocYear() {
		return matDocYear;
	}

	public void setMatDocYear(Integer matDocYear) {
		this.matDocYear = matDocYear;
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

	public String getSaleOrderProjectCode() {
		return saleOrderProjectCode;
	}

	public void setSaleOrderProjectCode(String saleOrderProjectCode) {
		this.saleOrderProjectCode = saleOrderProjectCode == null ? null : saleOrderProjectCode.trim();
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

	public Integer getReferReceiptId() {
		return referReceiptId;
	}

	public void setReferReceiptId(Integer referReceiptId) {
		this.referReceiptId = referReceiptId;
	}

	public String getReferReceiptCode() {
		return referReceiptCode;
	}

	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode == null ? null : referReceiptCode.trim();
	}

	public Integer getReferReceiptRid() {
		return referReceiptRid;
	}

	public void setReferReceiptRid(Integer referReceiptRid) {
		this.referReceiptRid = referReceiptRid;
	}

	public Integer getReferMatDocId() {
		return referMatDocId;
	}

	public void setReferMatDocId(Integer referMatDocId) {
		this.referMatDocId = referMatDocId;
	}

	public Integer getReferMatDocRid() {
		return referMatDocRid;
	}

	public void setReferMatDocRid(Integer referMatDocRid) {
		this.referMatDocRid = referMatDocRid;
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

	public Byte getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(Byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	public String getDebitCredit() {
		return debitCredit;
	}

	public void setDebitCredit(String debitCredit) {
		this.debitCredit = debitCredit == null ? null : debitCredit.trim();
	}

	public Byte getWriteOff() {
		return writeOff;
	}

	public void setWriteOff(Byte writeOff) {
		this.writeOff = writeOff;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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

	public String getMoveTypeCode() {
		return moveTypeCode;
	}

	public void setMoveTypeCode(String moveTypeCode) {
		this.moveTypeCode = moveTypeCode;
	}
}
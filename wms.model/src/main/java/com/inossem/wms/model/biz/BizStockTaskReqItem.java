package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockTaskReqItem {
	// -----------------------
	private String remark;
	private Integer referReceiptRid;
	private String referReceiptTypeName;
	private Byte referReceiptType;
	private Integer referReceiptId;
	private String referReceiptCode;
	private String packageTypeName;
	private BigDecimal packageStandard;
	private String packageStandardCh;
	private String packageTypeCode;
	private Byte matStoreType;
	
	private Integer packageTypeId;
	
	private String productionBatch;

	private String erpBatch;

	private String qualityBatch;
	// ------------------------

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReferReceiptTypeName() {
		return referReceiptTypeName;
	}

	public void setReferReceiptTypeName(String referReceiptTypeName) {
		this.referReceiptTypeName = referReceiptTypeName;
	}

	public Integer getReferReceiptRid() {
		return referReceiptRid;
	}

	public void setReferReceiptRid(Integer referReceiptRid) {
		this.referReceiptRid = referReceiptRid;
	}

	public Byte getReferReceiptType() {
		return referReceiptType;
	}

	public void setReferReceiptType(Byte referReceiptType) {
		this.referReceiptType = referReceiptType;
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
		this.referReceiptCode = referReceiptCode;
	}

	public String getPackageTypeName() {
		return packageTypeName;
	}

	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName;
	}

	public BigDecimal getPackageStandard() {
		return packageStandard;
	}

	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}

	public String getPackageStandardCh() {
		return packageStandardCh;
	}

	public void setPackageStandardCh(String packageStandardCh) {
		this.packageStandardCh = packageStandardCh;
	}

	public String getPackageTypeCode() {
		return packageTypeCode;
	}

	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
	}

	public String getProductionBatch() {
		return productionBatch;
	}

	public void setProductionBatch(String productionBatch) {
		this.productionBatch = productionBatch;
	}

	public String getErpBatch() {
		return erpBatch;
	}

	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}

	public String getQualityBatch() {
		return qualityBatch;
	}

	public void setQualityBatch(String qualityBatch) {
		this.qualityBatch = qualityBatch;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Byte getMatStoreType() {
		return matStoreType;
	}

	public void setMatStoreType(Byte matStoreType) {
		this.matStoreType = matStoreType;
	}

	private Integer itemId;

	private Integer stockTaskReqId;

	private Integer stockTaskReqRid;

	private Byte status;

	private Integer whId;

	private Integer matId;

	private Integer ftyId;

	private Integer locationId;

	private Long batch;

	private String specStock;

	private String specStockCode;

	private String specStockName;

	private BigDecimal qty;

	private Integer unitId;

	private Integer matDocId;

	private Integer matDocRid;

	private Date takeDeliveryDate;

	private BigDecimal weight;

	private Byte unitWeight;

	private BigDecimal stockTaskQty;

	private Date validityTime;

	private Date createTime;

	private Date modifyTime;

	private Byte isDelete;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStockTaskReqId() {
		return stockTaskReqId;
	}

	public void setStockTaskReqId(Integer stockTaskReqId) {
		this.stockTaskReqId = stockTaskReqId;
	}

	public Integer getStockTaskReqRid() {
		return stockTaskReqRid;
	}

	public void setStockTaskReqRid(Integer stockTaskReqRid) {
		this.stockTaskReqRid = stockTaskReqRid;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getWhId() {
		return whId;
	}

	public void setWhId(Integer whId) {
		this.whId = whId;
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

	public Date getTakeDeliveryDate() {
		return takeDeliveryDate;
	}

	public void setTakeDeliveryDate(Date takeDeliveryDate) {
		this.takeDeliveryDate = takeDeliveryDate;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Byte getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(Byte unitWeight) {
		this.unitWeight = unitWeight;
	}

	public BigDecimal getStockTaskQty() {
		return stockTaskQty;
	}

	public void setStockTaskQty(BigDecimal stockTaskQty) {
		this.stockTaskQty = stockTaskQty;
	}

	public Date getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(Date validityTime) {
		this.validityTime = validityTime;
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

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}
}
package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockTaskPositionCw {
	
	private Byte receiptType;
	
	private Integer sourcePalletId;
	private String sourcePalletCode;

	public Byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Byte receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getSourcePalletId() {
		return sourcePalletId;
	}

	public void setSourcePalletId(Integer sourcePalletId) {
		this.sourcePalletId = sourcePalletId;
	}

	public String getSourcePalletCode() {
		return sourcePalletCode;
	}

	public void setSourcePalletCode(String sourcePalletCode) {
		this.sourcePalletCode = sourcePalletCode;
	}

	// -----------------------
	private Integer referReceiptRid;
	
	private String inputTime;
	
	private String productionBatch;

	private String erpBatch;

	private String qualityBatch;
	
	
	
	private String unitZh;
	private Integer areaId;
	private Integer positionId;
	private String areaCode;
	private String positionCode;
	
	private BigDecimal maxPayload;
	private BigDecimal havePayload;
	private int packageNum;
	
	private String palletCode;
	private BigDecimal maxWeight;
	private String packageTypeCode;
	private BigDecimal packageStandard;
	private String packageStandardCh;
	private String packageCode;
	private String whCode;
	private String targetAreaCode;
	private String targetPositionCode;
	private String sourceAreaCode;
	private String sourcePositionCode;
	private Integer itemId;
	private Byte referReceiptType;
	private Integer referReceiptId;
	private String referReceiptCode;
	// -----------------------

	
	private Integer stockTaskId;

	
	public Integer getReferReceiptRid() {
		return referReceiptRid;
	}

	public void setReferReceiptRid(Integer referReceiptRid) {
		this.referReceiptRid = referReceiptRid;
	}

	public String getInputTime() {
		return inputTime;
	}

	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}

	public String getSourceAreaCode() {
		return sourceAreaCode;
	}

	public void setSourceAreaCode(String sourceAreaCode) {
		this.sourceAreaCode = sourceAreaCode;
	}

	public String getSourcePositionCode() {
		return sourcePositionCode;
	}

	public void setSourcePositionCode(String sourcePositionCode) {
		this.sourcePositionCode = sourcePositionCode;
	}

	public String getReferReceiptCode() {
		return referReceiptCode;
	}

	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode;
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

	public String getUnitZh() {
		return unitZh;
	}

	public void setUnitZh(String unitZh) {
		this.unitZh = unitZh;
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public BigDecimal getMaxPayload() {
		return maxPayload;
	}

	public void setMaxPayload(BigDecimal maxPayload) {
		this.maxPayload = maxPayload;
	}

	public BigDecimal getHavePayload() {
		return havePayload;
	}

	public void setHavePayload(BigDecimal havePayload) {
		this.havePayload = havePayload;
	}

	public int getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(int packageNum) {
		this.packageNum = packageNum;
	}

	public BigDecimal getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(BigDecimal maxWeight) {
		this.maxWeight = maxWeight;
	}

	public String getPalletCode() {
		return palletCode;
	}

	public void setPalletCode(String palletCode) {
		this.palletCode = palletCode;
	}

	public String getPackageTypeCode() {
		return packageTypeCode;
	}

	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
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

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getTargetAreaCode() {
		return targetAreaCode;
	}

	public void setTargetAreaCode(String targetAreaCode) {
		this.targetAreaCode = targetAreaCode;
	}

	public String getTargetPositionCode() {
		return targetPositionCode;
	}

	public void setTargetPositionCode(String targetPositionCode) {
		this.targetPositionCode = targetPositionCode;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	private Integer stockTaskRid;

	public Integer getStockTaskId() {
		return stockTaskId;
	}

	public void setStockTaskId(Integer stockTaskId) {
		this.stockTaskId = stockTaskId;
	}

	public Integer getStockTaskRid() {
		return stockTaskRid;
	}

	public void setStockTaskRid(Integer stockTaskRid) {
		this.stockTaskRid = stockTaskRid;
	}

	private Integer itemPositionId;

	private Integer stockTaskReqId;

	private Integer stockTaskReqRid;

	private Integer stockTaskReqPositionId;

	private Integer stockId;

	private Integer stockSnId;

	private Long batch;

	private Integer snId;

	private Integer palletId;

	private Integer packageId;

	private BigDecimal qty;

	private Integer whId;

	private Integer sourceAreaId;

	private Integer sourcePositionId;

	private BigDecimal stockQty;

	private String remark;

	private Byte isDelete;

	private Date createTime;

	private Date modifyTime;

	private String createUser;

	private String modifyUser;

	private Integer targetAreaId;

	private Integer targetPositionId;

	public Integer getItemPositionId() {
		return itemPositionId;
	}

	public void setItemPositionId(Integer itemPositionId) {
		this.itemPositionId = itemPositionId;
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

	public Integer getStockTaskReqPositionId() {
		return stockTaskReqPositionId;
	}

	public void setStockTaskReqPositionId(Integer stockTaskReqPositionId) {
		this.stockTaskReqPositionId = stockTaskReqPositionId;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public Integer getStockSnId() {
		return stockSnId;
	}

	public void setStockSnId(Integer stockSnId) {
		this.stockSnId = stockSnId;
	}

	public Long getBatch() {
		return batch;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
	}

	public Integer getSnId() {
		return snId;
	}

	public void setSnId(Integer snId) {
		this.snId = snId;
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

	public Integer getWhId() {
		return whId;
	}

	public void setWhId(Integer whId) {
		this.whId = whId;
	}

	public Integer getSourceAreaId() {
		return sourceAreaId;
	}

	public void setSourceAreaId(Integer sourceAreaId) {
		this.sourceAreaId = sourceAreaId;
	}

	public Integer getSourcePositionId() {
		return sourcePositionId;
	}

	public void setSourcePositionId(Integer sourcePositionId) {
		this.sourcePositionId = sourcePositionId;
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

	public Integer getTargetAreaId() {
		return targetAreaId;
	}

	public void setTargetAreaId(Integer targetAreaId) {
		this.targetAreaId = targetAreaId;
	}

	public Integer getTargetPositionId() {
		return targetPositionId;
	}

	public void setTargetPositionId(Integer targetPositionId) {
		this.targetPositionId = targetPositionId;
	}
}
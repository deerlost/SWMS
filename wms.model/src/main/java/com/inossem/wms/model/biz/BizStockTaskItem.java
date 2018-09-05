package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockTaskItem {
	private Integer targetPalletId;
	private Integer targetPackageId;
	private Integer snId;

	public Integer getTargetPalletId() {
		return targetPalletId;
	}

	public void setTargetPalletId(Integer targetPalletId) {
		this.targetPalletId = targetPalletId;
	}

	public Integer getTargetPackageId() {
		return targetPackageId;
	}

	public void setTargetPackageId(Integer targetPackageId) {
		this.targetPackageId = targetPackageId;
	}

	public Integer getSnId() {
		return snId;
	}

	public void setSnId(Integer snId) {
		this.snId = snId;
	}

	private Integer stockPositionId;

    public Integer getStockPositionId() {
		return stockPositionId;
	}

	public void setStockPositionId(Integer stockPositionId) {
		this.stockPositionId = stockPositionId;
	}
		
    private Integer itemId;

    private Integer stockTaskId;

    private Integer stockTaskRid;

    private Integer whId;

    private Integer matId;

    private Integer ftyId;

    private Long batch;

    private BigDecimal qty;

    private String specStock;

    private String specStockCode;

    private String specStockName;

    private Integer unitId;

    private BigDecimal weight;

    private Integer unitWeight;

    private Integer sourceStockType;

    private Integer sourceAreaId;

    private Integer sourcePositionId;

    private Integer targetStockType;

    private Integer targetAreaId;

    private Integer targetPositionId;

    private Integer locationId;

    private String referDeliveredCode;

    private String referDeliveredRid;

    private Integer stockTaskReqId;

    private Integer stockListingReqRid;

    private Integer matDocId;

    private Integer matDocYear;

    private Integer matDocRid;

    private Date createTime;

    private Date modifyTime;

    private Byte isDelete;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

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

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Integer unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Integer getSourceStockType() {
        return sourceStockType;
    }

    public void setSourceStockType(Integer sourceStockType) {
        this.sourceStockType = sourceStockType;
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

    public Integer getTargetStockType() {
        return targetStockType;
    }

    public void setTargetStockType(Integer targetStockType) {
        this.targetStockType = targetStockType;
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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getReferDeliveredCode() {
        return referDeliveredCode;
    }

    public void setReferDeliveredCode(String referDeliveredCode) {
        this.referDeliveredCode = referDeliveredCode == null ? null : referDeliveredCode.trim();
    }

    public String getReferDeliveredRid() {
        return referDeliveredRid;
    }

    public void setReferDeliveredRid(String referDeliveredRid) {
        this.referDeliveredRid = referDeliveredRid == null ? null : referDeliveredRid.trim();
    }

    public Integer getStockTaskReqId() {
        return stockTaskReqId;
    }

    public void setStockTaskReqId(Integer stockTaskReqId) {
        this.stockTaskReqId = stockTaskReqId;
    }

    public Integer getStockListingReqRid() {
        return stockListingReqRid;
    }

    public void setStockListingReqRid(Integer stockListingReqRid) {
        this.stockListingReqRid = stockListingReqRid;
    }

    public Integer getMatDocId() {
        return matDocId;
    }

    public void setMatDocId(Integer matDocId) {
        this.matDocId = matDocId;
    }

    public Integer getMatDocYear() {
        return matDocYear;
    }

    public void setMatDocYear(Integer matDocYear) {
        this.matDocYear = matDocYear;
    }

    public Integer getMatDocRid() {
        return matDocRid;
    }

    public void setMatDocRid(Integer matDocRid) {
        this.matDocRid = matDocRid;
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
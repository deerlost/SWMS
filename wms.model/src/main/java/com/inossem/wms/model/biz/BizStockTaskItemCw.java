package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BizStockTaskItemCw {
	
	//-----------------------
	private String remark;
	private Byte receiptType;
	private String productionBatch;

	private String erpBatch;

	private String qualityBatch;
	private Byte workModel;
	private Integer referReceiptRid;
	private Byte referReceiptType;
	private Integer referReceiptId;
	private String referReceiptCode;
	private Integer packageTypeId;
	private Byte matStoreType;
	private Integer beforeTaskId;

	private List<BizStockTaskPositionCw> palletPackageList;
	//-----------------------
	
    private Integer itemId;

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Byte receiptType) {
		this.receiptType = receiptType;
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

	public Byte getWorkModel() {
		return workModel;
	}

	public void setWorkModel(Byte workModel) {
		this.workModel = workModel;
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

	public List<BizStockTaskPositionCw> getPalletPackageList() {
		return palletPackageList;
	}

	public void setPalletPackageList(List<BizStockTaskPositionCw> palletPackageList) {
		this.palletPackageList = palletPackageList;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
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


	private Integer stockTaskId;

    private Integer stockTaskRid;

    private Integer stockTaskReqId;

    private Integer stockTaskReqRid;

    private Byte status;

    private Integer whId;

    private Integer ftyId;

    private Integer locationId;

    private Integer matId;

    private Integer unitId;

    private Long batch;

    private BigDecimal stockTaskQty;

    private BigDecimal qty;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    private Integer matDocId;

    private Integer matDocRid;

    private Date takeDeliveryDate;

    private Date validityTime;

    private BigDecimal weight;

    private Byte unitWeight;

    private String specStock;

    private String specStockCode;

    private String specStockName;

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

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public BigDecimal getStockTaskQty() {
        return stockTaskQty;
    }

    public void setStockTaskQty(BigDecimal stockTaskQty) {
        this.stockTaskQty = stockTaskQty;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
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

	public Integer getBeforeTaskId() {
		return beforeTaskId;
	}

	public void setBeforeTaskId(Integer beforeTaskId) {
		this.beforeTaskId = beforeTaskId;
	}
}
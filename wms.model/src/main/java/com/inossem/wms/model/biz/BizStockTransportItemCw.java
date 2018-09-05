package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BizStockTransportItemCw {
	//_______________________________
	// 转储入库id
	private Integer stockInputId;
	// 转储入库 rid
	private Integer stockInputRid;
	
	private List<BizStockTransportPositionCw> positionList;
	private Byte matStoreType;
	private Byte workModel;
	//_______________________________
	
	
    private Integer itemId;

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

	public Byte getWorkModel() {
		return workModel;
	}

	public void setWorkModel(Byte workModel) {
		this.workModel = workModel;
	}

	public Byte getMatStoreType() {
		return matStoreType;
	}

	public void setMatStoreType(Byte matStoreType) {
		this.matStoreType = matStoreType;
	}

	public List<BizStockTransportPositionCw> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<BizStockTransportPositionCw> positionList) {
		this.positionList = positionList;
	}

	private Integer stockTransportId;

    private Integer stockTransportRid;

    private Integer matId;

    private Integer packageTypeId;

    private BigDecimal stockQty;

    private String productionBatchInput;

    private String productionBatch;

    private String erpBatchInput;

    private String erpBatch;

    private String qualityBatchInput;

    private String qualityBatch;

    private Integer ftyOutput;

    private Integer locationOutput;

    private Long batch;

    private BigDecimal transportQty;

    private Integer ftyInput;

    private Integer locationInput;

    private Integer matInput;

    private Integer unitId;

    private Date createTime;

    private Date modifyTime;

    private Byte isDelete;

    private String remark;
    
    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getStockTransportId() {
        return stockTransportId;
    }

    public void setStockTransportId(Integer stockTransportId) {
        this.stockTransportId = stockTransportId;
    }

    public Integer getStockTransportRid() {
        return stockTransportRid;
    }

    public void setStockTransportRid(Integer stockTransportRid) {
        this.stockTransportRid = stockTransportRid;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public BigDecimal getStockQty() {
        return stockQty;
    }

    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
    }

    public String getProductionBatchInput() {
        return productionBatchInput;
    }

    public void setProductionBatchInput(String productionBatchInput) {
        this.productionBatchInput = productionBatchInput == null ? null : productionBatchInput.trim();
    }

    public String getProductionBatch() {
        return productionBatch;
    }

    public void setProductionBatch(String productionBatch) {
        this.productionBatch = productionBatch == null ? null : productionBatch.trim();
    }

    public String getErpBatchInput() {
        return erpBatchInput;
    }

    public void setErpBatchInput(String erpBatchInput) {
        this.erpBatchInput = erpBatchInput == null ? null : erpBatchInput.trim();
    }

    public String getErpBatch() {
        return erpBatch;
    }

    public void setErpBatch(String erpBatch) {
        this.erpBatch = erpBatch == null ? null : erpBatch.trim();
    }

    public String getQualityBatchInput() {
        return qualityBatchInput;
    }

    public void setQualityBatchInput(String qualityBatchInput) {
        this.qualityBatchInput = qualityBatchInput == null ? null : qualityBatchInput.trim();
    }

    public String getQualityBatch() {
        return qualityBatch;
    }

    public void setQualityBatch(String qualityBatch) {
        this.qualityBatch = qualityBatch == null ? null : qualityBatch.trim();
    }

    public Integer getFtyOutput() {
        return ftyOutput;
    }

    public void setFtyOutput(Integer ftyOutput) {
        this.ftyOutput = ftyOutput;
    }

    public Integer getLocationOutput() {
        return locationOutput;
    }

    public void setLocationOutput(Integer locationOutput) {
        this.locationOutput = locationOutput;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public BigDecimal getTransportQty() {
        return transportQty;
    }

    public void setTransportQty(BigDecimal transportQty) {
        this.transportQty = transportQty;
    }

    public Integer getFtyInput() {
        return ftyInput;
    }

    public void setFtyInput(Integer ftyInput) {
        this.ftyInput = ftyInput;
    }

    public Integer getLocationInput() {
        return locationInput;
    }

    public void setLocationInput(Integer locationInput) {
        this.locationInput = locationInput;
    }

    public Integer getMatInput() {
        return matInput;
    }

    public void setMatInput(Integer matInput) {
        this.matInput = matInput;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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
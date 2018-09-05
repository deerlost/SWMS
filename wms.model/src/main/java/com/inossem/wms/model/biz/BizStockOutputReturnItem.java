package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.inossem.wms.model.dic.DicBatchSpec;

public class BizStockOutputReturnItem {
	private String mesWriteOffCode;
	
	private String matWriteOffCode;	
	
	public String getMesWriteOffCode() {
		return mesWriteOffCode;
	}

	public void setMesWriteOffCode(String mesWriteOffCode) {
		this.mesWriteOffCode = mesWriteOffCode;
	}

	public String getMatWriteOffCode() {
		return matWriteOffCode;
	}

	public void setMatWriteOffCode(String matWriteOffCode) {
		this.matWriteOffCode = matWriteOffCode;
	}

	private String mesDocCode;

	public String getMesDocCode() {
		return mesDocCode;
	}

	public void setMesDocCode(String mesDocCode) {
		this.mesDocCode = mesDocCode;
	}
	
	private String matDocCode;

	public String getMatDocCode() {
		return matDocCode;
	}

	public void setMatDocCode(String matDocCode) {
		this.matDocCode = matDocCode;
	}
	
	private String erpBatch;


	public String getErpBatch() {
		return erpBatch;
	}

	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}

	private List<BizStockOutputReturnPosition> positionList;

	public List<BizStockOutputReturnPosition> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<BizStockOutputReturnPosition> positionList) {
		this.positionList = positionList;
	}
	
	private String remark;
	
	private String erpRemark;
	
	

	public String getErpRemark() {
		return erpRemark;
	}

	public void setErpRemark(String erpRemark) {
		this.erpRemark = erpRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	//------补充成本对象
	private String reserveCostObjCode;
    private String reserveCostObjName;
	

	
	//------补充是新建批次还是获取的最近批次
	
	public String getReserveCostObjCode() {
		return reserveCostObjCode;
	}

	public void setReserveCostObjCode(String reserveCostObjCode) {
		this.reserveCostObjCode = reserveCostObjCode;
	}

	public String getReserveCostObjName() {
		return reserveCostObjName;
	}

	public void setReserveCostObjName(String reserveCostObjName) {
		this.reserveCostObjName = reserveCostObjName;
	}

	boolean isRecentBatch ;

	public boolean isRecentBatch() {
		return isRecentBatch;
	}

	public void setRecentBatch(boolean isRecentBatch) {
		this.isRecentBatch = isRecentBatch;
	}

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

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
	}

	private Integer itemId;

    private Integer returnId;

    private Integer returnRid;

    private Integer stockOutputId;

    private Integer stockOutputRid;

    private String referReceiptCode;

    private String referReceiptRid;

    private Integer matId;

    private Integer unitId;

    private Integer ftyId;

    private Integer locationId;

    private BigDecimal qty;

    private Integer moveTypeId;

    private Long batch;

    private Long batchOutput;

    private String reserveId;

    private String reserveRid;

    private String purchaseOrderCode;

    private String purchaseOrderRid;

    private Byte writeOff;

    private BigDecimal saleQty;

    private BigDecimal sendQty;

    private Integer matDocId;

    private Integer matDocRid;

    private Byte isDelete;

    private Date modifyTime;

    private Date createTime;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        this.returnId = returnId;
    }

    public Integer getReturnRid() {
        return returnRid;
    }

    public void setReturnRid(Integer returnRid) {
        this.returnRid = returnRid;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(Integer moveTypeId) {
        this.moveTypeId = moveTypeId;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public Long getBatchOutput() {
        return batchOutput;
    }

    public void setBatchOutput(Long batchOutput) {
        this.batchOutput = batchOutput;
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

    public Byte getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(Byte writeOff) {
        this.writeOff = writeOff;
    }

    public BigDecimal getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(BigDecimal saleQty) {
        this.saleQty = saleQty;
    }

    public BigDecimal getSendQty() {
        return sendQty;
    }

    public void setSendQty(BigDecimal sendQty) {
        this.sendQty = sendQty;
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

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
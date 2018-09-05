package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockOutputReturnPosition {
	private Integer locationId;	
	private Integer unitId;	
	private Integer matId;

	
	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
	}

	private Integer itemStockTaskPositionId;//下架作业单仓位主键
	
	public Integer getItemStockTaskPositionId() {
		return itemStockTaskPositionId;
	}

	public void setItemStockTaskPositionId(Integer itemStockTaskPositionId) {
		this.itemStockTaskPositionId = itemStockTaskPositionId;
	}

	private Integer stockOutputItemId;//出库单行项目主键
	
	public Integer getStockOutputItemId() {
		return stockOutputItemId;
	}

	public void setStockOutputItemId(Integer stockOutputItemId) {
		this.stockOutputItemId = stockOutputItemId;
	}

	private Integer packageTypeId;

	private String productBatch;

	private String erpBatch;

	private String qualityBatch;
	
	
	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}


	public String getProductBatch() {
		return productBatch;
	}

	public void setProductBatch(String productBatch) {
		this.productBatch = productBatch;
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

	private BigDecimal outputQty;
	private Long outputBatch;

    public BigDecimal getOutputQty() {
		return outputQty;
	}

	public void setOutputQty(BigDecimal outputQty) {
		this.outputQty = outputQty;
	}

	public Long getOutputBatch() {
		return outputBatch;
	}

	public void setOutputBatch(Long outputBatch) {
		this.outputBatch = outputBatch;
	}

	private Integer itemOutputPositionId;
		
	
	public Integer getItemOutputPositionId() {
		return itemOutputPositionId;
	}

	public void setItemOutputPositionId(Integer itemOutputPositionId) {
		this.itemOutputPositionId = itemOutputPositionId;
	}

	private Integer itemPositionId;

    private Integer returnId;

    private Integer returnRid;

    private Integer returnPositionId;

    private Integer stockId;

    private Integer stockSnId;

    private Long batch;

    private Integer snId;

    private Integer palletId;

    private Integer packageId;

    private BigDecimal qty;

    private String specStock;

    private String specStockCode;

    private String specStockName;

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

    public Integer getItemPositionId() {
        return itemPositionId;
    }

    public void setItemPositionId(Integer itemPositionId) {
        this.itemPositionId = itemPositionId;
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

    public Integer getReturnPositionId() {
        return returnPositionId;
    }

    public void setReturnPositionId(Integer returnPositionId) {
        this.returnPositionId = returnPositionId;
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
}
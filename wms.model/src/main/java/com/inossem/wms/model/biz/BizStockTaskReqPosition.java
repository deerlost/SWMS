package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizStockTaskReqPosition {
	
	//---------------------
	
	private Integer packageTypeId;
	private String unitZh;
	private String packageTypeCode;
	
	
	private BigDecimal maxPayload;
	private BigDecimal havePayload;
	private int packageNum;
	//托盘最大载重
	private BigDecimal maxWeight;
	private String packageCode;
	private String palletCode;
	//---------------------
	
	private Byte status;
	
    public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public String getUnitZh() {
		return unitZh;
	}

	public void setUnitZh(String unitZh) {
		this.unitZh = unitZh;
	}

	public String getPackageTypeCode() {
		return packageTypeCode;
	}

	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
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

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPalletCode() {
		return palletCode;
	}

	public void setPalletCode(String palletCode) {
		this.palletCode = palletCode;
	}

	public BigDecimal getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(BigDecimal maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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
package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizAllocateCargoPosition {
	private Integer itemPositionId;

    private Integer allocateCargoId;

    private Integer allocateCargoRid;

    private Integer allocateCargoPositionId;

    private Integer stockId;

    private Long batch;

    private BigDecimal cargoQty;

    private String remark;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    private String createUser;

    private String modifyUser;
    
    private Integer matId;   
    
    private Integer packageTypeId;
    
    private Integer locationId;
    
    private  BigDecimal stockQty;
    
    private String inputDate;
    
    private Integer stockTypeId;
    
    private String productBatch;
        
    

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public Integer getStockTypeId() {
		return stockTypeId;
	}

	public void setStockTypeId(Integer stockTypeId) {
		this.stockTypeId = stockTypeId;
	}

	public String getProductBatch() {
		return productBatch;
	}

	public void setProductBatch(String productBatch) {
		this.productBatch = productBatch;
	}

	public BigDecimal getStockQty() {
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty) {
		this.stockQty = stockQty;
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

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getItemPositionId() {
        return itemPositionId;
    }

    public void setItemPositionId(Integer itemPositionId) {
        this.itemPositionId = itemPositionId;
    }

    public Integer getAllocateCargoId() {
        return allocateCargoId;
    }

    public void setAllocateCargoId(Integer allocateCargoId) {
        this.allocateCargoId = allocateCargoId;
    }

    public Integer getAllocateCargoRid() {
        return allocateCargoRid;
    }

    public void setAllocateCargoRid(Integer allocateCargoRid) {
        this.allocateCargoRid = allocateCargoRid;
    }

    public Integer getAllocateCargoPositionId() {
        return allocateCargoPositionId;
    }

    public void setAllocateCargoPositionId(Integer allocateCargoPositionId) {
        this.allocateCargoPositionId = allocateCargoPositionId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public BigDecimal getCargoQty() {
        return cargoQty;
    }

    public void setCargoQty(BigDecimal cargoQty) {
        this.cargoQty = cargoQty;
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
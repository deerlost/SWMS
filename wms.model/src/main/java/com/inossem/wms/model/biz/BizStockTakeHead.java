package com.inossem.wms.model.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BizStockTakeHead {
	private String workShiftName;
	
	
    public String getWorkShiftName() {
		return workShiftName;
	}

	public void setWorkShiftName(String workShiftName) {
		this.workShiftName = workShiftName;
	}

	private List<Map<String, Object>> itemList;
	
    private Integer stockTakeId;

    private String stockTakeCode;

    private Byte stockTakeType;

    private Byte stockTakeMode;

    private Date stockTakeTime;

    private Byte workShift;

    private Integer ftyId;

    private Integer locationId;

    private Byte stockTakeStatus;

    private String remark;

    private Byte status;

    private Byte stockTakeFreeze;

    private String createUser;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    private String createUserName;

    private String locationName;

    private String ftyName;

    public String getFtyName() {
		return ftyName;
	}

	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public List<Map<String, Object>> getItemList() {
		return itemList;
	}

	public void setItemList(List<Map<String, Object>> itemList) {
		this.itemList = itemList;
	}

	public Integer getStockTakeId() {
        return stockTakeId;
    }

    public void setStockTakeId(Integer stockTakeId) {
        this.stockTakeId = stockTakeId;
    }

    public String getStockTakeCode() {
        return stockTakeCode;
    }

    public void setStockTakeCode(String stockTakeCode) {
        this.stockTakeCode = stockTakeCode == null ? null : stockTakeCode.trim();
    }

    public Byte getStockTakeType() {
        return stockTakeType;
    }

    public void setStockTakeType(Byte stockTakeType) {
        this.stockTakeType = stockTakeType;
    }

    public Byte getStockTakeMode() {
        return stockTakeMode;
    }

    public void setStockTakeMode(Byte stockTakeMode) {
        this.stockTakeMode = stockTakeMode;
    }

    public Date getStockTakeTime() {
        return stockTakeTime;
    }

    public void setStockTakeTime(Date stockTakeTime) {
        this.stockTakeTime = stockTakeTime;
    }

    public Byte getWorkShift() {
        return workShift;
    }

    public void setWorkShift(Byte workShift) {
        this.workShift = workShift;
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

    public Byte getStockTakeStatus() {
        return stockTakeStatus;
    }

    public void setStockTakeStatus(Byte stockTakeStatus) {
        this.stockTakeStatus = stockTakeStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getStockTakeFreeze() {
        return stockTakeFreeze;
    }

    public void setStockTakeFreeze(Byte stockTakeFreeze) {
        this.stockTakeFreeze = stockTakeFreeze;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
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
}
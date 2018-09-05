package com.inossem.wms.model.biz;

import java.util.Date;
import java.util.List;

import com.inossem.wms.model.page.IPageResultCommon;

public class BizStockTransportHeadCw implements IPageResultCommon{
	//________________________________
	private List<BizReceiptAttachment> fileList;
	private Integer deliveryVehicleId;
	private Integer deliveryDriverId;
	
	
	private List<BizStockTransportItemCw> itemList;
	
	private String userName;
	private String ftyCode;
	private String locationCode;
	private String ftyName;
	private String locationName;
	private String classTypeName;
	//________________________________
	
	
	
    private Integer stockTransportId;


	public List<BizReceiptAttachment> getFileList() {
		return fileList;
	}

	public void setFileList(List<BizReceiptAttachment> fileList) {
		this.fileList = fileList;
	}

	public Integer getDeliveryVehicleId() {
		return deliveryVehicleId;
	}

	public void setDeliveryVehicleId(Integer deliveryVehicleId) {
		this.deliveryVehicleId = deliveryVehicleId;
	}

	public Integer getDeliveryDriverId() {
		return deliveryDriverId;
	}

	public void setDeliveryDriverId(Integer deliveryDriverId) {
		this.deliveryDriverId = deliveryDriverId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFtyCode() {
		return ftyCode;
	}

	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getFtyName() {
		return ftyName;
	}

	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public List<BizStockTransportItemCw> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizStockTransportItemCw> itemList) {
		this.itemList = itemList;
	}

	private String stockTransportCode;

    private Byte receiptType;

    private Integer moveTypeId;

    private Integer ftyId;

    private Integer locationId;

    private Byte status;

    private String remark;

    private Byte requestSource;

    private Byte isDelete;

    private String createUser;

    private Date createTime;

    private Date modifyTime;

    private String driver;

    private String vehicle;

    private Integer classTypeId;
    
    private Integer synType;

    public Integer getSynType() {
		return synType;
	}

	public void setSynType(Integer synType) {
		this.synType = synType;
	}

	public Integer getStockTransportId() {
        return stockTransportId;
    }

    public void setStockTransportId(Integer stockTransportId) {
        this.stockTransportId = stockTransportId;
    }

    public String getStockTransportCode() {
        return stockTransportCode;
    }

    public void setStockTransportCode(String stockTransportCode) {
        this.stockTransportCode = stockTransportCode == null ? null : stockTransportCode.trim();
    }

    public Byte getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Byte receiptType) {
        this.receiptType = receiptType;
    }

    public Integer getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(Integer moveTypeId) {
        this.moveTypeId = moveTypeId;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(Byte requestSource) {
        this.requestSource = requestSource;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
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

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver == null ? null : driver.trim();
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle == null ? null : vehicle.trim();
    }

    public Integer getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(Integer classTypeId) {
        this.classTypeId = classTypeId;
    }
    private int totalCount = -1;
    
    @Override
	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
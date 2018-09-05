package com.inossem.wms.model.biz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BizStockTransportHead {
    private Integer stockTransportId;

    private String stockTransportCode;

    private Byte receiptType;

    private Integer moveTypeId;

    private Integer ftyId;

    private Integer locationId;

    private Byte status;

    private String remark;

    private String createUser;

    private Date createTime;

    private Date modifyTime;

    private Byte isDelete;
    
    private Byte requestSource;
    

    public Byte getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Byte requestSource) {
		this.requestSource = requestSource;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getCreateTime() {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	if(createTime!=null){
    		return format.format(this.createTime);
    	}else{
    		return null;
    	}
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	if(modifyTime!=null){
    		return format.format(this.modifyTime);
    	}else{
    		return null;
    	}
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
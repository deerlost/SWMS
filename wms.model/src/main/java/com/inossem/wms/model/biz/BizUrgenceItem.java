package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizUrgenceItem {
    private Integer itemId;

    private Integer urgenceId;

    private Integer urgenceRid;

    private Integer matId;
    
    private String matName;

    private Integer unitId;
    
    private String unitCode;

    private BigDecimal qty;

    private Integer areaId;

    private Integer positionId;
    
    private String areaCode;
    
    private String positionCode;

    private Byte status;

    private Date operationDate;

    private String operator;

    private String relateReceiptCode;

    private Byte isDelete;

    private String createUser;

    private String modifyUser;

    private Date createTime;

    private Date modifyTime;
    
    private Integer relateReceiptId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getUrgenceId() {
        return urgenceId;
    }

    public void setUrgenceId(Integer urgenceId) {
        this.urgenceId = urgenceId;
    }

    public Integer getUrgenceRid() {
        return urgenceRid;
    }

    public void setUrgenceRid(Integer urgenceRid) {
        this.urgenceRid = urgenceRid;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getRelateReceiptCode() {
        return relateReceiptCode;
    }

    public void setRelateReceiptCode(String relateReceiptCode) {
        this.relateReceiptCode = relateReceiptCode == null ? null : relateReceiptCode.trim();
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

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public String getMatName() {
		return matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public Integer getRelateReceiptId() {
		return relateReceiptId;
	}

	public void setRelateReceiptId(Integer relateReceiptId) {
		this.relateReceiptId = relateReceiptId;
	}
    
    
}
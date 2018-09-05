package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BizStockOutputItem {
    private Integer itemId;

    private Integer stockOutputId;

    private Integer stockOutputRid;

    private Integer ftyId;

    private Integer matId;

    private Integer unitId;

    private Byte decimalPlace;

    private BigDecimal sendQty;

    private BigDecimal outputQty;

    private BigDecimal returnQty;

    private BigDecimal stockQty;

    private Integer locationId;

    private Integer moveTypeId;

    private String referReceiptCode;

    private String referReceiptRid;

    private BigDecimal referPrice;

    private Integer matDocId;

    private Integer matDocYear;

    private Integer matDocRid;

    private String reserveId;

    private String reserveRid;

    private Byte status;

    private String remark;

    private Byte isDelete;

    private Byte writeOff;

    private String createUser;

    private String modifyUser;

    private Date createTime;

    private Date modifyTime;

    private String productBatch;
	
	private String qualityBatch;
	
	private Integer packageTypeId;
	
	private String erpBatch;	
    
	private Long batch;
	
	private Integer whId;
	
	private String matDocCode;
	
	private String mesDocCode;
	
	private String mesWriteOffCode;
	
	private String matWriteOffCode;
	
	public String getMatWriteOffCode() {
		return matWriteOffCode;
	}

	public void setMatWriteOffCode(String matWriteOffCode) {
		this.matWriteOffCode = matWriteOffCode;
	}

	public String getMesWriteOffCode() {
		return mesWriteOffCode;
	}

	public void setMesWriteOffCode(String mesWriteOffCode) {
		this.mesWriteOffCode = mesWriteOffCode;
	}

	public String getMesDocCode() {
		return mesDocCode;
	}

	public void setMesDocCode(String mesDocCode) {
		this.mesDocCode = mesDocCode;
	}

	public String getMatDocCode() {
		return matDocCode;
	}

	public void setMatDocCode(String matDocCode) {
		this.matDocCode = matDocCode;
	}

	public String getProductBatch() {
		return productBatch;
	}

	public void setProductBatch(String productBatch) {
		this.productBatch = productBatch;
	}

	public String getQualityBatch() {
		return qualityBatch;
	}

	public void setQualityBatch(String qualityBatch) {
		this.qualityBatch = qualityBatch;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public String getErpBatch() {
		return erpBatch;
	}

	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}

	public Long getBatch() {
		return batch;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
	}

	public Integer getWhId() {
		return whId;
	}

	public void setWhId(Integer whId) {
		this.whId = whId;
	}

	public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
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

    public Byte getDecimalPlace() {
        return decimalPlace;
    }

    public void setDecimalPlace(Byte decimalPlace) {
        this.decimalPlace = decimalPlace;
    }

    public BigDecimal getSendQty() {
        return sendQty;
    }

    public void setSendQty(BigDecimal sendQty) {
        this.sendQty = sendQty;
    }

    public BigDecimal getOutputQty() {
        return outputQty;
    }

    public void setOutputQty(BigDecimal outputQty) {
        this.outputQty = outputQty;
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public BigDecimal getStockQty() {
        return stockQty;
    }

    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(Integer moveTypeId) {
        this.moveTypeId = moveTypeId;
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

    public BigDecimal getReferPrice() {
        return referPrice;
    }

    public void setReferPrice(BigDecimal referPrice) {
        this.referPrice = referPrice;
    }

    public Integer getMatDocId() {
        return matDocId;
    }

    public void setMatDocId(Integer matDocId) {
        this.matDocId = matDocId;
    }

    public Integer getMatDocYear() {
        return matDocYear;
    }

    public void setMatDocYear(Integer matDocYear) {
        this.matDocYear = matDocYear;
    }

    public Integer getMatDocRid() {
        return matDocRid;
    }

    public void setMatDocRid(Integer matDocRid) {
        this.matDocRid = matDocRid;
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

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Byte getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(Byte writeOff) {
        this.writeOff = writeOff;
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
}
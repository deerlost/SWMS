package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BizStockTransportItem {
    private Integer itemId;

    private Integer stockTransportId;

    private Integer stockTransportRid;

    private Integer stockPositionId;

    private Integer stockSnId;
    
    private Integer palletId;
    
    private Integer packageId;
    
    private Integer snId;
    
    private Integer matId;

    private Integer ftyId;

    private Integer locationId;

    private Long batch;

    private Integer whId;

    private Integer areaId;

    private Integer positionId;

    private BigDecimal transportQty;

    private Integer ftyInput;

    private Integer locationInput;

    private Integer matInput;

    private String specStockCodeInput;

    private String specStockInput;

    private String specStockCodeOutput;

    private String specStockNameOutput;
    
    private String specStockOutput;

    private Integer unitId;

    private Date createTime;

    private Date modifyTime;

    private Byte isDelete;

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

    public Integer getStockPositionId() {
        return stockPositionId;
    }

    public void setStockPositionId(Integer stockPositionId) {
        this.stockPositionId = stockPositionId;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
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

    public String getSpecStockCodeInput() {
        return specStockCodeInput;
    }

    public void setSpecStockCodeInput(String specStockCodeInput) {
        this.specStockCodeInput = specStockCodeInput == null ? null : specStockCodeInput.trim();
    }

    public String getSpecStockCodeOutput() {
        return specStockCodeOutput;
    }

    public void setSpecStockCodeOutput(String specStockCodeOutput) {
        this.specStockCodeOutput = specStockCodeOutput == null ? null : specStockCodeOutput.trim();
    }

    public String getSpecStockInput() {
		return specStockInput;
	}

	public void setSpecStockInput(String specStockInput) {
		this.specStockInput = specStockInput;
	}

	public String getSpecStockOutput() {
		return specStockOutput;
	}

	public void setSpecStockOutput(String specStockOutput) {
		this.specStockOutput = specStockOutput;
	}

	public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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

	public String getSpecStockNameOutput() {
		return specStockNameOutput;
	}

	public void setSpecStockNameOutput(String specStockNameOutput) {
		this.specStockNameOutput = specStockNameOutput;
	}

	public Integer getStockSnId() {
		return stockSnId;
	}

	public void setStockSnId(Integer stockSnId) {
		this.stockSnId = stockSnId;
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

	public Integer getSnId() {
		return snId;
	}

	public void setSnId(Integer snId) {
		this.snId = snId;
	}
    
    
}
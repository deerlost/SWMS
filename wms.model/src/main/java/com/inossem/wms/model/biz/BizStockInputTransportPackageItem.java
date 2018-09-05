package com.inossem.wms.model.biz;

import java.math.BigDecimal;

public class BizStockInputTransportPackageItem {

	//--------------------
	private String packageTypeCode;
    private String packageTypeName;
    private String packageStandardCh;
    private BigDecimal packageStandard;
	//---------------------
	
	private Long batch;
	
    public String getPackageTypeCode() {
		return packageTypeCode;
	}

	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
	}

	public String getPackageTypeName() {
		return packageTypeName;
	}

	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName;
	}

	public String getPackageStandardCh() {
		return packageStandardCh;
	}

	public void setPackageStandardCh(String packageStandardCh) {
		this.packageStandardCh = packageStandardCh;
	}

	public BigDecimal getPackageStandard() {
		return packageStandard;
	}

	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}

	public Long getBatch() {
		return batch;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
	}

	private Integer packageTypeItemId;

    private Integer stockInputItemId;

    private Integer packageTypeId;

    private Integer packageNum;

    private BigDecimal qty;

    private String productionBatch;

    private String erpBatch;

    private String qualityBatch;

    private String remark;

    public Integer getPackageTypeItemId() {
        return packageTypeItemId;
    }

    public void setPackageTypeItemId(Integer packageTypeItemId) {
        this.packageTypeItemId = packageTypeItemId;
    }

    public Integer getStockInputItemId() {
        return stockInputItemId;
    }

    public void setStockInputItemId(Integer stockInputItemId) {
        this.stockInputItemId = stockInputItemId;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public Integer getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(Integer packageNum) {
        this.packageNum = packageNum;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getProductionBatch() {
        return productionBatch;
    }

    public void setProductionBatch(String productionBatch) {
        this.productionBatch = productionBatch == null ? null : productionBatch.trim();
    }

    public String getErpBatch() {
        return erpBatch;
    }

    public void setErpBatch(String erpBatch) {
        this.erpBatch = erpBatch == null ? null : erpBatch.trim();
    }

    public String getQualityBatch() {
        return qualityBatch;
    }

    public void setQualityBatch(String qualityBatch) {
        this.qualityBatch = qualityBatch == null ? null : qualityBatch.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}

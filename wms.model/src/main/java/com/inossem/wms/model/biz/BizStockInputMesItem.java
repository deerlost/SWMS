package com.inossem.wms.model.biz;

import java.math.BigDecimal;

public class BizStockInputMesItem {
    private Integer mesItemId;

    private Integer stockInputItemId;

    private String matCode;

    private String packageNum;

    private BigDecimal qty;
   
	private BigDecimal mesPackageStandard;
	
	private String mesRank;
	
	private String mesBch;
	
	private String mesLocationId;
	
	private String mesLocationName;
	
    public String getMesLocationId() {
		return mesLocationId;
	}

	public void setMesLocationId(String mesLocationId) {
		this.mesLocationId = mesLocationId;
	}

	public String getMesLocationName() {
		return mesLocationName;
	}

	public void setMesLocationName(String mesLocationName) {
		this.mesLocationName = mesLocationName;
	}

	public String getMesBch() {
		return mesBch;
	}

	public void setMesBch(String mesBch) {
		this.mesBch = mesBch;
	}

	public String getMesRank() {
		return mesRank;
	}

	public void setMesRank(String mesRank) {
		this.mesRank = mesRank;
	}

	public BigDecimal getMesPackageStandard() {
		return mesPackageStandard;
	}

	public void setMesPackageStandard(BigDecimal mesPackageStandard) {
		this.mesPackageStandard = mesPackageStandard;
	}

	public Integer getMesItemId() {
        return mesItemId;
    }

    public void setMesItemId(Integer mesItemId) {
        this.mesItemId = mesItemId;
    }

    public Integer getStockInputItemId() {
        return stockInputItemId;
    }

    public void setStockInputItemId(Integer stockInputItemId) {
        this.stockInputItemId = stockInputItemId;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode == null ? null : matCode.trim();
    }

    public String getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(String packageNum) {
        this.packageNum = packageNum == null ? null : packageNum.trim();
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
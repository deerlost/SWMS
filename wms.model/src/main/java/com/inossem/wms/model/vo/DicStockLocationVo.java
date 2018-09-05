package com.inossem.wms.model.vo;

import com.inossem.wms.model.dic.DicStockLocation;

public class DicStockLocationVo extends DicStockLocation {
	
	private Byte ftyType;
	private String ftyCode;
	private String ftyName;
	private String corpCode;
	private String corpName;
	
	
	public Byte getFtyType() {
		return ftyType;
	}
	public void setFtyType(Byte ftyType) {
		this.ftyType = ftyType;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getFtyCode() {
		return ftyCode;
	}
	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}
	public String getFtyName() {
		return ftyName;
	}
	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	
}

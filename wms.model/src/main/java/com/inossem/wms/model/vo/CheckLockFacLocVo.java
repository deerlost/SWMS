package com.inossem.wms.model.vo;

import com.inossem.wms.model.dic.DicStockLocation;

/**
 * 共通函数:出入库冻结用
 * @author wang.ligang
 *
 */
public class CheckLockFacLocVo extends DicStockLocation {
	
    private Integer corpId;
    private String ftyCode;
    private String ftyName;
    private String corpName;
    
	public Integer getCorpId() {
		return corpId;
	}
	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
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

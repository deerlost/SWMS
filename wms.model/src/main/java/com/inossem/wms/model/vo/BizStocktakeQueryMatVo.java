package com.inossem.wms.model.vo;

/**
 * 新增物资-添加物料查询物料信息列表接口用VO
 * @author wang.ligang
 *
 */
public class BizStocktakeQueryMatVo {
	private Integer whId;
    private String whCode;
    private Integer matId;
    private String matCode;
//    private Integer unitId;
    private String unitName;
    private BizStocktakeQueryMatInfoVo matInfo;
    
    
	public Integer getWhId() {
		return whId;
	}
	public void setWhId(Integer whId) {
		this.whId = whId;
	}
	public String getWhCode() {
		return whCode;
	}
	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
	public Integer getMatId() {
		return matId;
	}
	public void setMatId(Integer matId) {
		this.matId = matId;
	}
	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
//	public Integer getUnitId() {
//		return unitId;
//	}
//	public void setUnitId(Integer unitId) {
//		this.unitId = unitId;
//	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public BizStocktakeQueryMatInfoVo getMatInfo() {
		return matInfo;
	}
	public void setMatInfo(BizStocktakeQueryMatInfoVo matInfo) {
		this.matInfo = matInfo;
	}
    
    
}

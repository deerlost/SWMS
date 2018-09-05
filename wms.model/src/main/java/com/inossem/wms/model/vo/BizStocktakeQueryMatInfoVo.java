package com.inossem.wms.model.vo;

/**
 * 新增物资-添加物料查询物料信息列表接口用VO
 * @author wang.ligang
 *
 */
public class BizStocktakeQueryMatInfoVo {
	private String matName;
	private Integer unitId;
    private String unitCode;
    private Integer matGroupId;
    private String matGroupCode;
    private String matGroupName;
    private Byte decimalPlace; 
    
    
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public Integer getMatGroupId() {
		return matGroupId;
	}
	public void setMatGroupId(Integer matGroupId) {
		this.matGroupId = matGroupId;
	}
	public String getMatGroupCode() {
		return matGroupCode;
	}
	public void setMatGroupCode(String matGroupCode) {
		this.matGroupCode = matGroupCode;
	}
	public String getMatGroupName() {
		return matGroupName;
	}
	public void setMatGroupName(String matGroupName) {
		this.matGroupName = matGroupName;
	}
	public Byte getDecimalPlace() {
		return decimalPlace;
	}
	public void setDecimalPlace(Byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}
    
}

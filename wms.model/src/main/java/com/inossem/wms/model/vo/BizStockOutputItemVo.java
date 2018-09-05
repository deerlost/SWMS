package com.inossem.wms.model.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockOutputItem;

public class BizStockOutputItemVo extends BizStockOutputItem {

	private String stockOutputCode;
	
	private String ftyCode;
	
	private String ftyName;
	
	private String locationCode;
	
	private String locationName;

	private String mesLocationCode;
	
	private String matCode;
	
	private String matName;
	
	private String unitCode;
	
	private String unitName;
	
	private String moveTypeCode;
	
	private String moveTypeName;
	
	private String writeOffName;
	
	private String isFocusBatch;
		
	private String whCode;
	
	private String whName;
	
	private String areaCode;
	
	private String areaName;
	
	private String positionCode;

	private String inputDate;
	
	private String snCode;
	
	private String packageCode;
	
	private String packageTypeCode;
	
	private String packageTypeName;
	
	private String packageStandardCh;
	
	private BigDecimal packageStandard;
	
	private String sampName;
	
	private String specification;
	
	private String standard;
	
	private List<BizStockOutputPositionVo> positionList;
	
	private List<Map<String,Object>> taskList;
	
	private List<Map<String,Object>> locationList;
	
	private List<Map<String,Object>> erpbatchList;
	
	public String getSampName() {
		return sampName;
	}

	public void setSampName(String sampName) {
		this.sampName = sampName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getMesLocationCode() {
		return mesLocationCode;
	}

	public void setMesLocationCode(String mesLocationCode) {
		this.mesLocationCode = mesLocationCode;
	}

	public BigDecimal getPackageStandard() {
		return packageStandard;
	}

	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}

	public String getPackageStandardCh() {
		return packageStandardCh;
	}

	public void setPackageStandardCh(String packageStandardCh) {
		this.packageStandardCh = packageStandardCh;
	}

	public List<Map<String, Object>> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Map<String, Object>> locationList) {
		this.locationList = locationList;
	}

	public List<Map<String, Object>> getErpbatchList() {
		return erpbatchList;
	}

	public void setErpbatchList(List<Map<String, Object>> erpbatchList) {
		this.erpbatchList = erpbatchList;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getWhName() {
		return whName;
	}

	public void setWhName(String whName) {
		this.whName = whName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

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

	public List<Map<String, Object>> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Map<String, Object>> taskList) {
		this.taskList = taskList;
	}

	public String getIsFocusBatch() {
		return isFocusBatch;
	}

	public void setIsFocusBatch(String isFocusBatch) {
		this.isFocusBatch = isFocusBatch;
	}

	public String getStockOutputCode() {
		return stockOutputCode;
	}

	public void setStockOutputCode(String stockOutputCode) {
		this.stockOutputCode = stockOutputCode;
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

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
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

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public String getMoveTypeCode() {
		return moveTypeCode;
	}

	public void setMoveTypeCode(String moveTypeCode) {
		this.moveTypeCode = moveTypeCode;
	}

	public String getMoveTypeName() {
		return moveTypeName;
	}

	public void setMoveTypeName(String moveTypeName) {
		this.moveTypeName = moveTypeName;
	}

	public String getWriteOffName() {
		return writeOffName;
	}

	public void setWriteOffName(String writeOffName) {
		this.writeOffName = writeOffName;
	}

	public List<BizStockOutputPositionVo> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<BizStockOutputPositionVo> positionList) {
		this.positionList = positionList;
	}
	
	
}

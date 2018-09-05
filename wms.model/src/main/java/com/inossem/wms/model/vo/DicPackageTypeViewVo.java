package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class DicPackageTypeViewVo extends DicPackageType  {
	private String unitCode;
	private String unitName;
	private String classificatName;
	private String nameZh;
	private String matUnitCode;
	private String matUunitName;

	private String matNameZh;

	
	public String getMatUnitCode() {
		return matUnitCode;
	}
	public void setMatUnitCode(String matUnitCode) {
		this.matUnitCode = matUnitCode;
	}
	public String getMatUunitName() {
		return matUunitName;
	}
	public void setMatUunitName(String matUunitName) {
		this.matUunitName = matUunitName;
	}
	public String getMatNameZh() {
		return matNameZh;
	}
	public void setMatNameZh(String matNameZh) {
		this.matNameZh = matNameZh;
	}
	public String getNameZh() {
		return nameZh;
	}
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
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
	public String getClassificatName() {
		return classificatName;
	}
	public void setClassificatName(String classificatName) {
		this.classificatName = classificatName;
	}
	

}

package com.inossem.wms.model;

import java.util.List;
import java.util.Map;

/** 
* @author 高海涛 
* @version 2017年12月1日
* Excel导入 列属性 
*/
public class ExcelCellType {

	public ExcelCellType(){
		
	}
	
	public ExcelCellType(boolean notNull){
		this.notNull = notNull;
	}
	
	public ExcelCellType(boolean notNull,int length,String type){
		this.notNull = notNull;
		this.length = length;
		this.type = type;
	}
	
	public ExcelCellType(boolean needValidation,Map<String,List<String>> validationData){
		this.needValidation = needValidation;
		this.validationData = validationData;
	}
	
	public ExcelCellType(boolean notNull,int length,String type ,boolean needValidation,Map<String,List<String>> validationData){
		this.notNull = notNull;
		this.length = length;
		this.type = type;
		this.needValidation = needValidation;
		this.validationData = validationData;
	}

	private boolean notNull = false;
	
	private int length = 0;
	
	private String type = "";
	
	private boolean needValidation = false;
	
	private Map<String,List<String>> validationData = null;
	
	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNeedValidation() {
		return needValidation;
	}

	public void setNeedValidation(boolean needValidation) {
		this.needValidation = needValidation;
	}

	public Map<String, List<String>> getValidationData() {
		return validationData;
	}

	public void setValidationData(Map<String, List<String>> validationData) {
		this.validationData = validationData;
	}
	
}

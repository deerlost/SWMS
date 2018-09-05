package com.inossem.wms.model.vo;

import java.io.Serializable;

public class BizStoktakeItemNewVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;

    private Integer itemId;

    private String purchaseOrderCode;

    private String contractCode;

    private String supplierCode;

    private String supplierName;

    private String demandDept;

    private Integer corpId;
    private String corpCode;

    private String inputTime;

    private String productionTime;

    private String validityTime;

    private String remark;
    private int decimalPlace;
    
    
    public BizStoktakeItemNewVo() {
    	
    }
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getPurchaseOrderCode() {
		return purchaseOrderCode;
	}
	public void setPurchaseOrderCode(String purchaseOrderCode) {
		this.purchaseOrderCode = purchaseOrderCode;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getDemandDept() {
		return demandDept;
	}
	public void setDemandDept(String demandDept) {
		this.demandDept = demandDept;
	}
	public Integer getCorpId() {
		return corpId;
	}
	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getInputTime() {
		return inputTime;
	}
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
	public String getProductionTime() {
		return productionTime;
	}
	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}
	public String getValidityTime() {
		return validityTime;
	}
	public void setValidityTime(String validityTime) {
		this.validityTime = validityTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getDecimalPlace() {
		return decimalPlace;
	}
	public void setDecimalPlace(int decimalPlace) {
		this.decimalPlace = decimalPlace;
	}
    
}

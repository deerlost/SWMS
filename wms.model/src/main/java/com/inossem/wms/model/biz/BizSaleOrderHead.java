package com.inossem.wms.model.biz;

public class BizSaleOrderHead {


	/**
	 * SAP[BUKRS] 公司代码
	 */
	private String corpCode;
	
	/**
	 * SAP[BUTXT] 公司名称
	 */
	private String corpName;
	/**
	 * SAP[VBELN] 销售订单号
	 */
	private String referReceiptCode;

	/**
	 * SAP[BSTNK] 合同号
	 */
	private String preorderId;
	/**
	 * SAP[KUNNR] 客户编码
	 */
	private String supplierCode;

	/**
	 * SAP[NAME1] 供应商名称--客户名称
	 */
	private String supplierName;
	
	/**
	 * SAP[AUART] 订单类型
	 */
	private String orderType;
	
	/**
	 * SAP[BEZEI] 订单类型名称
	 */
	private String orderTypeName;
	
	/**
	 * SAP[VKORG] 销售组织
	 */
	private String saleOrgCode;
	
	/**
	 * SAP[VTEXT] 销售组织名称
	 */
	private String saleOrgName;
	
	
	private String saleGroup;//sap没返回
	
	
	public String getSaleGroup() {
		return saleGroup;
	}


	public void setSaleGroup(String saleGroup) {
		this.saleGroup = saleGroup;
	}


	/**
	 * SAP[VKGRP] 销售组描述
	 */
	private String saleGroupName;
	
	

	public String getSaleGroupName() {
		return saleGroupName;
	}


	public void setSaleGroupName(String saleGroupName) {
		this.saleGroupName = saleGroupName;
	}


	/**
	 * SAP[ERDAT] 创建日期
	 */
	private String createTime;


	public String getCorpCode() {
		return corpCode;
	}


	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}


	public String getCorpName() {
		return corpName;
	}


	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}


	public String getReferReceiptCode() {
		return referReceiptCode;
	}


	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode;
	}


	public String getPreorderId() {
		return preorderId;
	}


	public void setPreorderId(String preorderId) {
		this.preorderId = preorderId;
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


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public String getOrderTypeName() {
		return orderTypeName;
	}


	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}


	public String getSaleOrgCode() {
		return saleOrgCode;
	}


	public void setSaleOrgCode(String saleOrgCode) {
		this.saleOrgCode = saleOrgCode;
	}


	public String getSaleOrgName() {
		return saleOrgName;
	}


	public void setSaleOrgName(String saleOrgName) {
		this.saleOrgName = saleOrgName;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	

	
	

}

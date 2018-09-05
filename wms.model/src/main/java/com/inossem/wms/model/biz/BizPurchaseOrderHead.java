package com.inossem.wms.model.biz;

import java.util.List;

/**
 * 采购订单
 * @author 666
 *
 */
public class BizPurchaseOrderHead {

	/**
	 * SAP[LIFNR] 供应商编码
	 */
	private String supplierCode;
	/**
	 * SAP[NAME1] 供应商名称
	 */
	private String supplierName;
	
	/**
	 * SAP[AEDAT] 创建日期
	 */
	private String createTime;
	
	/**
	 * SAP[] 使用单位id
	 */
	private Integer corpId;
	
	/**
	 * SAP[BUKRS] 使用单位代码
	 */
	private String corpCode;
	
	/**
	 * SAP[BUTXT] 使用单位
	 */
	private String corpName;
	
	/**
	 * SAP[BSART] 订单类型
	 */
	private String purchaseOrderType;
	
	/**
	 * SAP[BATXT] 订单类型描述
	 */
	private String purchaseOrderTypeName;
	
	/**
	 * SAP[EBELN] 采购订单编号
	 */
	private String purchaseOrderCode;

	/**
	 * SAP[EKGRP] 采购组
	 */
	private String purchaseGroup;
	
	/**
	 * SAP[EKNAM] 采购组描述
	 */
	private String purchaseGroupName;
	
	/**
	 * SAP[EKORG] 采购组织
	 */
	private String purchaseOrg;
	
	/**
	 * SAP[EKOTX] 采购组织描述
	 */
	private String purchaseOrgName;
	
	/**
	 * SAP[ZHTBH] 合同编号
	 */
	private String contractCode;
	
	/**
	 * SAP[ZHTMC] 合同描述
	 */
	private String contractName;
	
	private List<BizPurchaseOrderItem> itemList;

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

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

	public String getPurchaseOrderType() {
		return purchaseOrderType;
	}

	public void setPurchaseOrderType(String purchaseOrderType) {
		this.purchaseOrderType = purchaseOrderType;
	}

	public String getPurchaseOrderTypeName() {
		return purchaseOrderTypeName;
	}

	public void setPurchaseOrderTypeName(String purchaseOrderTypeName) {
		this.purchaseOrderTypeName = purchaseOrderTypeName;
	}

	public String getPurchaseOrderCode() {
		return purchaseOrderCode;
	}

	public void setPurchaseOrderCode(String purchaseOrderCode) {
		this.purchaseOrderCode = purchaseOrderCode;
	}

	public String getPurchaseGroup() {
		return purchaseGroup;
	}

	public void setPurchaseGroup(String purchaseGroup) {
		this.purchaseGroup = purchaseGroup;
	}

	public String getPurchaseGroupName() {
		return purchaseGroupName;
	}

	public void setPurchaseGroupName(String purchaseGroupName) {
		this.purchaseGroupName = purchaseGroupName;
	}

	public String getPurchaseOrg() {
		return purchaseOrg;
	}

	public void setPurchaseOrg(String purchaseOrg) {
		this.purchaseOrg = purchaseOrg;
	}

	public String getPurchaseOrgName() {
		return purchaseOrgName;
	}

	public void setPurchaseOrgName(String purchaseOrgName) {
		this.purchaseOrgName = purchaseOrgName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	

	public List<BizPurchaseOrderItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizPurchaseOrderItem> itemList) {
		this.itemList = itemList;
	}

	public Integer getCorpId() {
		return corpId;
	}

	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}
}

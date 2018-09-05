package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BizDeliveryOrderHead {	
	//销售订单号
	private String saleOrderCode;
	//合同编号
	private String preorderId;

	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	

	public String getPreorderId() {
		return preorderId;
	}

	public void setPreorderId(String preorderId) {
		this.preorderId = preorderId;
	}



	private String remark;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private List<Map<String, Object>> classTypeList;

	public List<Map<String, Object>> getClassTypeList() {
		return classTypeList;
	}

	public void setClassTypeList(List<Map<String, Object>> classTypeList) {
		this.classTypeList = classTypeList;
	}

	private List<BizDeliveryOrderItem> itemList;

    public List<BizDeliveryOrderItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizDeliveryOrderItem> itemList) {
		this.itemList = itemList;
	}

	/**
	 * SAP[e_likp_id] 交货单号
	 */
	private Integer deliveryId;
	  	  
	/**
	 * SAP[VBELN] 交货单号
	 */
	private String deliveryCode;

	/**
	 * SAP[LFART] 交货单类型
	 */
	private String deliveryType;

	/**
	 * SAP[VBELN_s] 销售凭证
	 */
	private String saleDocument;
	
	/**
	 * SAP[ERDAT] 创建日期
	 */
	private String createTime;

	/**
	 * SAP[ERNAM] 创建人
	 */
	private String createUserId;

	/**
	 * SAP[ERNAM] 创建人
	 */
	private String createUserName;
	
	/**
	 * SAP[AUART] 销售凭证类型
	 */
	private String saleDocumentType;
	
	/**
	 * SAP[BEZEI] 类型描述
	 */
	private String saleDocumentTypeName;

	/**
	 * SAP[VKORG] 销售组织
	 */
	private String saleOrgCode;

	/**
	 * SAP[VTWEG] 分销渠道
	 */
	private String distributionChannels;

	/**
	 * SAP[SPART] 产品组
	 */
	private String productGroup;

	/**
	 * SAP[KUNNR] 客户编号
	 */
	private String customerCode;
	
	/**
	 * SAP[NAME1] 客户名称
	 */
	private String customerName;

	/**
	 * SAP[NAME1] 参考采购订单编号
	 */
	private String referPurchaseCode;


	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getSaleDocument() {
		return saleDocument;
	}

	public void setSaleDocument(String saleDocument) {
		this.saleDocument = saleDocument;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	
	

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getSaleDocumentType() {
		return saleDocumentType;
	}

	public void setSaleDocumentType(String saleDocumentType) {
		this.saleDocumentType = saleDocumentType;
	}

	public String getSaleDocumentTypeName() {
		return saleDocumentTypeName;
	}

	public void setSaleDocumentTypeName(String saleDocumentTypeName) {
		this.saleDocumentTypeName = saleDocumentTypeName;
	}

	public String getSaleOrgCode() {
		return saleOrgCode;
	}

	public void setSaleOrgCode(String saleOrgCode) {
		this.saleOrgCode = saleOrgCode;
	}

	public String getDistributionChannels() {
		return distributionChannels;
	}

	public void setDistributionChannels(String distributionChannels) {
		this.distributionChannels = distributionChannels;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getReferPurchaseCode() {
		return referPurchaseCode;
	}

	public void setReferPurchaseCode(String referPurchaseCode) {
		this.referPurchaseCode = referPurchaseCode;
	}
	
	
	

}

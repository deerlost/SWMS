package com.inossem.wms.model.sap;

import java.util.List;

/**
 * sap 查询交货单信息抬头
 * @author sw
 *
 */
public class SapDeliveryOrderHead {
	
	//交货单号
	private String deliveryOrderCode;
	//销售订单号
	private String saleOrderCode;
	//合同编号
	private String contractNumber;
	//客户编号
    private String receiveCode;
	//客户名称
	private String receiveName;
	//类型
	private String orderType;
	//类型描述
	private String orderTypeName;
	//参考采购订单编号
	private String referSaleOrderCode;
	//销售组织
	private String orgCode;
	//行项目list
	List<SapDeliveryOrderItem> itemList;
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public List<SapDeliveryOrderItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SapDeliveryOrderItem> itemList) {
		this.itemList = itemList;
	}
	public String getDeliveryOrderCode() {
		return deliveryOrderCode;
	}
	public void setDeliveryOrderCode(String deliveryOrderCode) {
		this.deliveryOrderCode = deliveryOrderCode;
	}
	public String getSaleOrderCode() {
		return saleOrderCode;
	}
	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getReceiveCode() {
		return receiveCode;
	}
	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public String getReferSaleOrderCode() {
		return referSaleOrderCode;
	}
	public void setReferSaleOrderCode(String referSaleOrderCode) {
		this.referSaleOrderCode = referSaleOrderCode;
	}
	
	
}

package com.inossem.wms.model.sap;

import java.math.BigDecimal;

/**
 * sap 查询交货单信息行项目
 * @author sw
 *
 */
public class SapDeliveryOrderItem {

	//行序号
	private String deliveryOrderRid;
	//物料编码
	private String matCode;
	//工厂编码
	private String ftyCode;
	//库存地点编码
	private String locationCode;
	//erp批次
    private String erpBatch;
    //erp备注
    private String erpRemark;
	//交货数
    private BigDecimal qty;
    //单位编码
    private String unitCode;
   
	public String getErpRemark() {
		return erpRemark;
	}
	public void setErpRemark(String erpRemark) {
		this.erpRemark = erpRemark;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getDeliveryOrderRid() {
		return deliveryOrderRid;
	}
	public void setDeliveryOrderRid(String deliveryOrderRid) {
		this.deliveryOrderRid = deliveryOrderRid;
	}
	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	public String getFtyCode() {
		return ftyCode;
	}
	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getErpBatch() {
		return erpBatch;
	}
	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
    
}

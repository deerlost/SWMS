package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BizDeliveryOrderItem {
	
	private List<Map<String, Object> > locList;
	
	
	public List<Map<String, Object>> getLocList() {
		return locList;
	}

	public void setLocList(List<Map<String, Object>> locList) {
		this.locList = locList;
	}

	private List<Map<String, Object>> erpBatchList;
	
	public List<Map<String, Object>> getErpBatchList() {
		return erpBatchList;
	}

	public void setErpBatchList(List<Map<String, Object>> erpBatchList) {
		this.erpBatchList = erpBatchList;
	}

	private BigDecimal canReturnQty;
	
	public BigDecimal getCanReturnQty() {
		return canReturnQty;
	}

	public void setCanReturnQty(BigDecimal canReturnQty) {
		this.canReturnQty = canReturnQty;
	}

	private String erpRemark;

	public String getErpRemark() {
		return erpRemark;
	}

	public void setErpRemark(String erpRemark) {
		this.erpRemark = erpRemark;
	}

	private Integer rid;

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	private BigDecimal returnQty;
	
	public BigDecimal getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(BigDecimal returnQty) {
		this.returnQty = returnQty;
	}

	private BigDecimal haveShelvesQty;

	public BigDecimal getHaveShelvesQty() {
		return haveShelvesQty;
	}

	public void setHaveShelvesQty(BigDecimal haveShelvesQty) {
		this.haveShelvesQty = haveShelvesQty;
	}
	
	private String remark;
	
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * SAP[e_lips_id] 
	 */
	private String itemId;
	
	/**
	 * SAP[VBELN] 交货单号
	 */
	private String deliveryCode;
	
	/**
	 * SAP[POSNR] 交货行项目
	 */
	private String deliveryRid;
	
	/**
	 * SAP[MATNR] 物料
	 */
	private String matCode;
	private Integer matId;
	private String matName;
	
	//单位
	private String unitCode;
	private Integer unitId;
	private String unitName;
	
	
	/**
	 * SAP[WERKS] 工厂
	 */
	private String ftyCode;
	private Integer ftyId;
	private String ftyName;
	
	
	/**
	 * SAP[LGORT] 库存地点
	 */
	private String locationCode;
	private Integer locationId;
	private String locationName;
	/**
	 * SAP[CHARG] 批号
	 */
	private String erpBatch;
	
	/**
	 * SAP[LFIMG] 交货数量
	 */
	private BigDecimal deliveryQty;
	
	/**
	 * SAP[VGBEL] 参考单据的单据编号
	 */
	private String referReceiptCode;
	
	/**
	 * SAP[VGPOS] 参考项目的项目号
	 */
	private String referReceiptRid;
	
	/**
	 * SAP[KDMAT] 客户物料编号
	 */
	private String customerMatCode;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getDeliveryRid() {
		return deliveryRid;
	}

	public void setDeliveryRid(String deliveryRid) {
		this.deliveryRid = deliveryRid;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getFtyCode() {
		return ftyCode;
	}

	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}

	public Integer getFtyId() {
		return ftyId;
	}

	public void setFtyId(Integer ftyId) {
		this.ftyId = ftyId;
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

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getErpBatch() {
		return erpBatch;
	}

	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}

	public BigDecimal getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(BigDecimal deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	public String getReferReceiptCode() {
		return referReceiptCode;
	}

	public void setReferReceiptCode(String referReceiptCode) {
		this.referReceiptCode = referReceiptCode;
	}

	public String getReferReceiptRid() {
		return referReceiptRid;
	}

	public void setReferReceiptRid(String referReceiptRid) {
		this.referReceiptRid = referReceiptRid;
	}

	public String getCustomerMatCode() {
		return customerMatCode;
	}

	public void setCustomerMatCode(String customerMatCode) {
		this.customerMatCode = customerMatCode;
	}
	
	
	
}

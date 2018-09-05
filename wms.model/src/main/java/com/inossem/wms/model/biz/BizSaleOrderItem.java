package com.inossem.wms.model.biz;

import java.util.List;

import com.inossem.wms.model.dic.DicBatchSpec;

public class BizSaleOrderItem {
	//值为空的批次号
	private String batch;

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	private int rid;//序号

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}
	
	//获取新批次
	private List<DicBatchSpec> batchSpecList;
	private List<DicBatchSpec> batchMaterialSpecList;

	public List<DicBatchSpec> getBatchSpecList() {
		return batchSpecList;
	}

	public void setBatchSpecList(List<DicBatchSpec> batchSpecList) {
		this.batchSpecList = batchSpecList;
	}

	public List<DicBatchSpec> getBatchMaterialSpecList() {
		return batchMaterialSpecList;
	}

	public void setBatchMaterialSpecList(List<DicBatchSpec> batchMaterialSpecList) {
		this.batchMaterialSpecList = batchMaterialSpecList;
	}

	//增加matId locationId locationName ftyId ftyName
	

	private int matId;
	
	private int locationId;
		
	private String locationName;
		
	private int ftyId;
		
	private String ftyName;

			
	public int getMatId() {
		return matId;
	}

	public void setMatId(int matId) {
		this.matId = matId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public int getFtyId() {
		return ftyId;
	}

	public void setFtyId(int ftyId) {
		this.ftyId = ftyId;
	}

	public String getFtyName() {
		return ftyName;
	}

	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}

	/**
	 * SAP[matnr] 物料号
	 */
	private String matCode;	
	
	/**
	 * SAP[ARKTX] 物料描述
	 */
	private String matName;
	
	/**
	 * SAP[POSNR] 行项目
	 */
	
	private String saleRid;
	
	
	/**
	 * SAP[VBELN] 销售订单号
	 */
	
	private String saleOrderCode;
	
	
	
	// item.put("rspox", itemTmp.getString("RSPOS")); 接口文档有，返回json无	
	//item.put("vbeln", itemTmp.getString("VBELN")); // 销售凭证 
	
	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	/**
	 * SAP[ZMENG] 销售数量 -- 销售退库中的订单数量 数据库中的saleQty
	 */
	private String orderQty;
	

	/**
	 * SAP[WERKS] 工厂编码
	 */
	private String ftyCode;
	
	/**
	 * SAP[LGORT] 库存地点
	 */
	private String locationCode;
	
	private int unitId;

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	/**
	 * SAP[MEINS] 计量单位
	 */
	private String unitEn;
	
//	item.put("auart", itemTmp.getString("AUART")); // 接口文档有，返回json无
	
	/**
	 * SAP[MSEHL] 单位描述
	 */
	private String unitName;
		
	//item.put("zcjsl", itemTmp.getString("ZCJSL")); // 已交货数量就是最大退库数量

	/**
	 * SAP[ZFHSL] 已发货数量就是已退库数量 数据库中的sendQty have_return_qty
	 */
	private String haveReturnQty;
	
	
	
	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getHaveReturnQty() {
		return haveReturnQty;
	}

	public void setHaveReturnQty(String haveReturnQty) {
		this.haveReturnQty = haveReturnQty;
	}

	/**
	 * SAP[SOBKZ] 特殊库存类型
	 */
	private String batchSpecType;
	
	/**
	 * SAP[ZTSKC] 特殊库存编号
	 */
	private String batchSpecCode;
	
	/**
	 * SAP[ZTSMS] 特殊库存描述
	 */
	private String batchSpecValue;
	
	/**
	 * SAP[ANDEC] 小数位数
	 */
	private String decimalPlace;

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

	public String getSaleRid() {
		return saleRid;
	}

	public void setSaleRid(String saleRid) {
		this.saleRid = saleRid;
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

	public String getUnitEn() {
		return unitEn;
	}

	public void setUnitEn(String unitEn) {
		this.unitEn = unitEn;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	public String getBatchSpecType() {
		return batchSpecType;
	}

	public void setBatchSpecType(String batchSpecType) {
		this.batchSpecType = batchSpecType;
	}

	public String getBatchSpecCode() {
		return batchSpecCode;
	}

	public void setBatchSpecCode(String batchSpecCode) {
		this.batchSpecCode = batchSpecCode;
	}

	public String getBatchSpecValue() {
		return batchSpecValue;
	}

	public void setBatchSpecValue(String batchSpecValue) {
		this.batchSpecValue = batchSpecValue;
	}

	public String getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(String decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	
	

}

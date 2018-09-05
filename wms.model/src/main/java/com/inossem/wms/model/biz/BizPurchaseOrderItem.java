package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.List;

import com.inossem.wms.model.dic.DicBatchSpec;

public class BizPurchaseOrderItem {


	private Integer stockInputRid;
	
	
	public Integer getStockInputRid() {
		return stockInputRid;
	}

	public void setStockInputRid(Integer stockInputRid) {
		this.stockInputRid = stockInputRid;
	}

	/**
	 * 批次特性
	 */
	private List<DicBatchSpec> batchSpecList;
	
	/**
	 * 批次信息
	 */
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

	private Integer matId;
	
	
	
	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
	}

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
	 * SAP[BUKRS] 使用单位代码
	 */
	private String corpCode;
	/**
	 * SAP[] 使用单位代码
	 */
	private String corpId;
	
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
	 * SAP[EBELP] 采购订单行项目序号
	 */
	private String purchaseOrderRid;
	

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
	
	/**
	 * SAP[WERKS] 工厂代码
	 */
	private String ftyCode;
	
	/**
	 * SAP[] 工厂id
	 */
	private Integer ftyId;
	
	/**
	 * SAP[] 工厂描述
	 */
	private String ftyName;
	
	/**
	 * SAP[LGORT] 库存地点
	 */
	private String locationCode;
	
	/**
	 * SAP[] 库存地点id
	 */
	private Integer locationId;
	
	/**
	 * SAP[] 库存地点描述
	 */
	private String locationName;
	
	/**
	 * SAP[MATNR] 物料编码
	 */
	private String matCode;
	
	/**
	 * SAP[TXZ01] 物料描述
	 */
	private String matName;
	
	/**
	 * SAP[MEINS] 计量单位
	 */
	private String unitCode;
	
	/**
	 * SAP[] 计量单位id
	 */
	private Integer unitId;
	
	/**
	 * SAP[MSEHL] 单位中文名称
	 */
	private String nameZh;
	
	/**
	 * SAP[ANDEC] 小数位
	 */
	private Byte decimalPlace;
	
	/**
	 * SAP[MENGE] 订单数量
	 */
	private BigDecimal orderQty;
	
	/**
	 * SAP[SJNUM] 已入库数量
	 */
	private BigDecimal inputedQty;
	
	/**
	 * SAP[AFNAM] 生产厂家
	 */
	private String manufacturer;
	
	/**
	 * SAP[POSID] 成本对象代码
	 */
	private String costObjCode;
	
	/**
	 * SAP[ZPOST1] 成本对象描述
	 */
	private String costObjName;

	/**
	 * SAP[ZTSKC] 特殊库存代码
	 */
	private String specStockCode;
	
	/**
	 * SAP[ZTSMS] 特殊库存描述
	 */
	private String specStockName;
	
	/**
	 * SAP[SOBKZ] 特殊库存标识
	 */
	private String specStock;
	
	/**
	 * SAP[TXT50] 需求部门
	 */
	private String demandDept;
	
	/**
	 * SAP[MATKL] 物料组
	 */
	private String matGroupCode;
	
	/**
	 * SAP[RETPO] 退货标识
	 */
	private String returnMarking;

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

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
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

	public String getPurchaseOrderRid() {
		return purchaseOrderRid;
	}

	public void setPurchaseOrderRid(String purchaseOrderRid) {
		this.purchaseOrderRid = purchaseOrderRid;
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

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	public Byte getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(Byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}

	public BigDecimal getInputedQty() {
		return inputedQty;
	}

	public void setInputedQty(BigDecimal inputedQty) {
		this.inputedQty = inputedQty;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getCostObjCode() {
		return costObjCode;
	}

	public void setCostObjCode(String costObjCode) {
		this.costObjCode = costObjCode;
	}

	public String getCostObjName() {
		return costObjName;
	}

	public void setCostObjName(String costObjName) {
		this.costObjName = costObjName;
	}

	public String getSpecStockCode() {
		return specStockCode;
	}

	public void setSpecStockCode(String specStockCode) {
		this.specStockCode = specStockCode;
	}

	public String getSpecStockName() {
		return specStockName;
	}

	public void setSpecStockName(String specStockName) {
		this.specStockName = specStockName;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
	}

	public String getDemandDept() {
		return demandDept;
	}

	public void setDemandDept(String demandDept) {
		this.demandDept = demandDept;
	}

	public String getMatGroupCode() {
		return matGroupCode;
	}

	public void setMatGroupCode(String matGroupCode) {
		this.matGroupCode = matGroupCode;
	}

	public String getReturnMarking() {
		return returnMarking;
	}

	public void setReturnMarking(String returnMarking) {
		this.returnMarking = returnMarking;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
	
}

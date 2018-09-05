package com.inossem.wms.model.biz;

import java.util.List;
import com.inossem.wms.model.dic.DicBatchSpec;

public class BizReserveOrderItem {
	private String isFocusBatch;// 批次信息是否强控
	
	
	
	public String getIsFocusBatch() {
		return isFocusBatch;
	}

	public void setIsFocusBatch(String isFocusBatch) {
		this.isFocusBatch = isFocusBatch;
	}

	// 值为空的批次号
	private String batch;

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	private int rid;// 序号

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	// 获取新批次
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

	// 增加matId locationId locationName ftyId ftyName

	private int matId;

	private int locationId;

	private String locationName;

	private int ftyId;

	private String ftyName;

	private int unitId;

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

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
	 * SAP[RSNUM] 预留号
	 */
	private String referReceiptCode;// reserve_id

	/**
	 * SAP[RSPOS] 行项目
	 */
	private String referReceiptRid;// reserve_rid

	/**
	 * SAP[matnr] 物料号
	 */
	private String matCode;

	/**
	 * SAP[MAKTX] 物料描述
	 */
	private String matName;

	private String moveTypeId; // 移动类型

	/**
	 * SAP[BWART] 预留移动类型
	 */
	private String moveTypeCode;

	/**
	 * SAP[SOBKZ] 特殊库存标识
	 */
	private String specStockCode;

	/**
	 * SAP[MEINS] 单位
	 */
	private String unitEn;

	/**
	 * SAP[MSEHL] 单位描述
	 */
	private String unitName;

	/**
	 * SAP[BDMNG] 需求数量
	 */
	private String demandQty;

	/**
	 * SAP[ENMNG] 已提货数量
	 */
	private String haveReturnQty;

	/**
	 * SAP[LBKUM] 总库存数量
	 */
	private String stockQty;

	/**
	 * SAP[SQLAB] 可发货数量
	 */
	private String canRreturnQty;

	/**
	 * SAP[ZPOSID] 成本对象
	 */
	private String reserveCostObjCode;

	/**
	 * SAP[ZPOST1] 成本对象
	 */
	private String reserveCostObjName;

	// 单位小数位数
	private byte decimalPlace;

	/**
	 * SAP[WERKS] 工厂编码
	 */
	private String ftyCode;

	/**
	 * SAP[LGORT] 库存地点
	 */
	private String locationCode;

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

	public String getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(String moveTypeId) {
		this.moveTypeId = moveTypeId;
	}

	public String getMoveTypeCode() {
		return moveTypeCode;
	}

	public void setMoveTypeCode(String moveTypeCode) {
		this.moveTypeCode = moveTypeCode;
	}

	public String getSpecStockCode() {
		return specStockCode;
	}

	public void setSpecStockCode(String specStockCode) {
		this.specStockCode = specStockCode;
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

	public String getDemandQty() {
		return demandQty;
	}

	public void setDemandQty(String demandQty) {
		this.demandQty = demandQty;
	}

	public String getHaveReturnQty() {
		return haveReturnQty;
	}

	public void setHaveReturnQty(String haveReturnQty) {
		this.haveReturnQty = haveReturnQty;
	}

	public String getStockQty() {
		return stockQty;
	}

	public void setStockQty(String stockQty) {
		this.stockQty = stockQty;
	}

	public String getCanRreturnQty() {
		return canRreturnQty;
	}

	public void setCanRreturnQty(String canRreturnQty) {
		this.canRreturnQty = canRreturnQty;
	}

	public String getReserveCostObjCode() {
		return reserveCostObjCode;
	}

	public void setReserveCostObjCode(String reserveCostObjCode) {
		this.reserveCostObjCode = reserveCostObjCode;
	}

	public String getReserveCostObjName() {
		return reserveCostObjName;
	}

	public void setReserveCostObjName(String reserveCostObjName) {
		this.reserveCostObjName = reserveCostObjName;
	}

	public byte getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(byte decimalPlace) {
		this.decimalPlace = decimalPlace;
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

}

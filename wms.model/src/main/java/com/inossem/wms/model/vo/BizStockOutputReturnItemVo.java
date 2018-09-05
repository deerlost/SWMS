package com.inossem.wms.model.vo;

import java.math.BigDecimal;
import java.util.List;

import com.inossem.wms.model.biz.BizStockOutputReturnItem;
import com.inossem.wms.model.dic.DicBatchSpec;

public class BizStockOutputReturnItemVo extends BizStockOutputReturnItem{
	
	private String postingDate;
	
	
	public String getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	
	
	//序号
	private Integer rid;

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
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
	
	
	private String referReceiptCode;
	private String referReceiptRid;	

	private Integer returnId;
	private Integer returnRid;
	
	private Integer matId;
	private String matCode;
	private String matName;
	
	private Integer unitId;
	private String unitEn;
	private String unitName;
	private byte decimalPlace;
	
	private Integer ftyId;
	private String ftyCode;
	private String ftyName;
	
	private Integer locationId;
	private String locationCode;
	private String locationName;

	
	private Integer moveTypeId;
	private String moveTypeCode;
	private String moveTypeName;
  
 
	//private BigDecimal qty; //本次退货数量
	private BigDecimal orderQty;   // 订单数量  需求数量saleQty
	private BigDecimal haveReturnQty; //已退货数量sendQty
	private BigDecimal demandQty; //需求数量saleQty
	
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
	public Integer getReturnId() {
		return returnId;
	}
	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}
	public Integer getReturnRid() {
		return returnRid;
	}
	public void setReturnRid(Integer returnRid) {
		this.returnRid = returnRid;
	}
	public Integer getMatId() {
		return matId;
	}
	public void setMatId(Integer matId) {
		this.matId = matId;
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
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	public byte getDecimalPlace() {
		return decimalPlace;
	}
	public void setDecimalPlace(byte decimalPlace) {
		this.decimalPlace = decimalPlace;
	}
	public Integer getFtyId() {
		return ftyId;
	}
	public void setFtyId(Integer ftyId) {
		this.ftyId = ftyId;
	}
	public String getFtyCode() {
		return ftyCode;
	}
	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode;
	}
	public String getFtyName() {
		return ftyName;
	}
	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Integer getMoveTypeId() {
		return moveTypeId;
	}
	public void setMoveTypeId(Integer moveTypeId) {
		this.moveTypeId = moveTypeId;
	}
	public String getMoveTypeCode() {
		return moveTypeCode;
	}
	public void setMoveTypeCode(String moveTypeCode) {
		this.moveTypeCode = moveTypeCode;
	}
	public String getMoveTypeName() {
		return moveTypeName;
	}
	public void setMoveTypeName(String moveTypeName) {
		this.moveTypeName = moveTypeName;
	}
//	public BigDecimal getQty() {
//		return qty;
//	}
//	public void setQty(BigDecimal qty) {
//		this.qty = qty;
//	}
	public BigDecimal getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}
	public BigDecimal getHaveReturnQty() {
		return haveReturnQty;
	}
	public void setHaveReturnQty(BigDecimal haveRreturnQty) {
		this.haveReturnQty = haveRreturnQty;
	}
	public BigDecimal getDemandQty() {
		return demandQty;
	}
	public void setDemandQty(BigDecimal demandQty) {
		this.demandQty = demandQty;
	}

	
	
	
}

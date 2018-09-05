package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.enums.EnumStocktakePositionStatus;
import com.inossem.wms.model.enums.EnumStocktakeStatus;
import com.inossem.wms.model.enums.EnumStocktakeStockStatus;

/**
 * 2.6	根据盘点凭证号获取盘点凭证基本信息 返回VO
 * @author wlg
 *
 */
public class BizStocktakeBaseInfoVo {
	private Integer stocktakeId;
	private String stocktakeCode;
	private Byte status;
	private String statusName;
	private String beginDate;
	private String endDate;
	private String userName;
	private Byte stocktakeType;
	private String remark;
	
	
	private Integer ftyId;
	
	private String ftyCode;
	private String ftyName;
	
	private Integer locationId;
	private String locationCode;
	private String locationName;
	
	private Byte positionStatus; 
	private String positionStatusName; 
	
	private Byte stockStatus;
	private String stockStatusName;
	
	private String specStockCode;
	private String specStockName;
	private Integer isSelfCommited;
	private Integer isSelfRecheck;
	
	private Byte freezeStocktake;
	
	private List<BizStocktakeBaseInfoItemVo> itemList;
	List<ApproveUserVo> userList;

	public Integer getStocktakeId() {
		return stocktakeId;
	}

	public void setStocktakeId(Integer stocktakeId) {
		this.stocktakeId = stocktakeId;
	}

	public String getStocktakeCode() {
		return stocktakeCode;
	}

	public void setStocktakeCode(String stocktakeCode) {
		this.stocktakeCode = stocktakeCode;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
		if(status != null) {
			//this.statusName = EnumStocktakeStatus.getDescByValue(status.toString());
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Byte getStocktakeType() {
		return stocktakeType;
	}

	public void setStocktakeType(Byte stocktakeType) {
		this.stocktakeType = stocktakeType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Byte getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(Byte positionStatus) {
		this.positionStatus = positionStatus;
		if(positionStatus != null){
			this.positionStatusName = EnumStocktakePositionStatus.getDescByValue(positionStatus.toString());
		}
	}

	public Byte getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(Byte stockStatus) {
		this.stockStatus = stockStatus;
		if(stockStatus != null){
			//this.stockStatusName = EnumStocktakeStockStatus.getDescByValue(stockStatus.toString());
		}
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

	public Byte getFreezeStocktake() {
		return freezeStocktake;
	}

	public void setFreezeStocktake(Byte freezeStocktake) {
		this.freezeStocktake = freezeStocktake;
	}

	public List<BizStocktakeBaseInfoItemVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizStocktakeBaseInfoItemVo> itemList) {
		this.itemList = itemList;
	}

	public Integer getIsSelfCommited() {
		return isSelfCommited;
	}

	public void setIsSelfCommited(Integer isSelfCommited) {
		this.isSelfCommited = isSelfCommited;
	}

	public Integer getIsSelfRecheck() {
		return isSelfRecheck;
	}

	public void setIsSelfRecheck(Integer isSelfRecheck) {
		this.isSelfRecheck = isSelfRecheck;
	}

	public List<ApproveUserVo> getUserList() {
		return userList;
	}

	public void setUserList(List<ApproveUserVo> userList) {
		this.userList = userList;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPositionStatusName() {
		return positionStatusName;
	}

	public void setPositionStatusName(String positionStatusName) {
		this.positionStatusName = positionStatusName;
	}

	public String getStockStatusName() {
		return stockStatusName;
	}

	public void setStockStatusName(String stockStatusName) {
		this.stockStatusName = stockStatusName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	
}

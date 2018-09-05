package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.biz.BizStockTransportHead;

public class BizStockTransportHeadVo extends BizStockTransportHead{
	
	private String receiptTypeName;
	
	private String statusName;
	
	private String ftyCode;
	
	private String ftyName;
	
	private String locationCode;
	
	private String locationName;
	
	private String moveTypeCode;
	
	private String moveTypeName;
	
	private String specStock;

	List<BizStockTransportItemVo> itemList;
	
	List<ApproveUserVo> userList;
	
	List<BizReceiptAttachmentVo> fileList;
	
	
	
	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
	}

	public String getReceiptTypeName() {
		return receiptTypeName;
	}

	public void setReceiptTypeName(String receiptTypeName) {
		this.receiptTypeName = receiptTypeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

	public List<BizStockTransportItemVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizStockTransportItemVo> itemList) {
		this.itemList = itemList;
	}

	public List<ApproveUserVo> getUserList() {
		return userList;
	}

	public void setUserList(List<ApproveUserVo> userList) {
		this.userList = userList;
	}

	public List<BizReceiptAttachmentVo> getFileList() {
		return fileList;
	}

	public void setFileList(List<BizReceiptAttachmentVo> fileList) {
		this.fileList = fileList;
	}
	
	
}

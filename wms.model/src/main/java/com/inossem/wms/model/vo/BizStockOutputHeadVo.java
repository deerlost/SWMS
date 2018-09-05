package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.biz.BizStockOutputHead;

public class BizStockOutputHeadVo extends BizStockOutputHead {
	
	private String userName;

	private String receiptTypeName;
	
	private String corpCode;
	
	private String corpName;
	
	private String statusName;
	
	private String classTypeName;
	
	private Integer ftyId;
	
	private String ftyCode;
	
	private String ftyName;
	
	private Integer locationId;
	
	private String locationCode;
	
	private String locationName;
	
	private String moveTypeCode;
	
	private String moveTypeName;

	List<BizStockOutputItemVo> itemList;
	
	List<ApproveUserVo> userList;
	
	List<BizReceiptAttachmentVo> fileList;
	
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
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
	
	public String getReceiptTypeName() {
		return receiptTypeName;
	}

	public void setReceiptTypeName(String receiptTypeName) {
		this.receiptTypeName = receiptTypeName;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<BizStockOutputItemVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<BizStockOutputItemVo> itemList) {
		this.itemList = itemList;
	}

	
	
	
	
}

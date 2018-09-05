package com.inossem.wms.model.vo;

import com.inossem.wms.model.biz.BizAllocateHead;

public class BizAllocateHeadVo extends BizAllocateHead {
	
	private String ftyInputCode;
	
	private String locationInputCode;
	
	
	public String getFtyInputCode() {
		return ftyInputCode;
	}
	public void setFtyInputCode(String ftyInputCode) {
		this.ftyInputCode = ftyInputCode;
	}
	public String getLocationInputCode() {
		return locationInputCode;
	}
	public void setLocationInputCode(String locationInputCode) {
		this.locationInputCode = locationInputCode;
	}
	
	
	private String orgName;
	private String userName;
	private String corpName;
	private String ftyName;
	private String locationName;
	private String ftyOutput;
	private String locationOutput;
	private String ftyOutputName;
	private String locationOutputName;
	
	private int totalCount = -1;
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getFtyName() {
		return ftyName;
	}
	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getFtyOutput() {
		return ftyOutput;
	}
	public void setFtyOutput(String ftyOutput) {
		this.ftyOutput = ftyOutput;
	}
	public String getLocationOutput() {
		return locationOutput;
	}
	public void setLocationOutput(String locationOutput) {
		this.locationOutput = locationOutput;
	}
	public String getFtyOutputName() {
		return ftyOutputName;
	}
	public void setFtyOutputName(String ftyOutputName) {
		this.ftyOutputName = ftyOutputName;
	}
	public String getLocationOutputName() {
		return locationOutputName;
	}
	public void setLocationOutputName(String locationOutputName) {
		this.locationOutputName = locationOutputName;
	}
	
	
}

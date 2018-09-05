package com.inossem.wms.model.dic;

import java.util.Date;

public class DicFactory {
    private Integer ftyId;

    private Integer corpId;

    private String ftyCode;

    private String ftyName;

    private String address;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;
    
    private Integer ftyType;
    
    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public String getFtyCode() {
        return ftyCode;
    }

    public void setFtyCode(String ftyCode) {
        this.ftyCode = ftyCode == null ? null : ftyCode.trim();
    }

    public String getFtyName() {
        return ftyName;
    }

    public void setFtyName(String ftyName) {
        this.ftyName = ftyName == null ? null : ftyName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

	public Integer getFtyType() {
		return ftyType;
	}

	public void setFtyType(Integer ftyType) {
		this.ftyType = ftyType;
	}
}
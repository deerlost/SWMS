package com.inossem.wms.model.biz;

import java.util.Date;

public class BizAssetCard {
    private Integer assetCardId;

    private String assetCardCode;

    private String assetCardName;

    private Integer stockOutputId;

    private Integer stockOutputRid;

    private Integer stockOutputPositionId;

    private Integer matReqId;

    private Integer matReqRid;

    private Integer matDocId;

    private Integer matDocRid;

    private Integer matDocYear;

    private String isSend;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getAssetCardId() {
        return assetCardId;
    }

    public void setAssetCardId(Integer assetCardId) {
        this.assetCardId = assetCardId;
    }

    public String getAssetCardCode() {
        return assetCardCode;
    }

    public void setAssetCardCode(String assetCardCode) {
        this.assetCardCode = assetCardCode == null ? null : assetCardCode.trim();
    }

    public String getAssetCardName() {
        return assetCardName;
    }

    public void setAssetCardName(String assetCardName) {
        this.assetCardName = assetCardName == null ? null : assetCardName.trim();
    }

    public Integer getStockOutputId() {
        return stockOutputId;
    }

    public void setStockOutputId(Integer stockOutputId) {
        this.stockOutputId = stockOutputId;
    }

    public Integer getStockOutputRid() {
        return stockOutputRid;
    }

    public void setStockOutputRid(Integer stockOutputRid) {
        this.stockOutputRid = stockOutputRid;
    }

    public Integer getStockOutputPositionId() {
        return stockOutputPositionId;
    }

    public void setStockOutputPositionId(Integer stockOutputPositionId) {
        this.stockOutputPositionId = stockOutputPositionId;
    }

    public Integer getMatReqId() {
        return matReqId;
    }

    public void setMatReqId(Integer matReqId) {
        this.matReqId = matReqId;
    }

    public Integer getMatReqRid() {
        return matReqRid;
    }

    public void setMatReqRid(Integer matReqRid) {
        this.matReqRid = matReqRid;
    }

    public Integer getMatDocId() {
        return matDocId;
    }

    public void setMatDocId(Integer matDocId) {
        this.matDocId = matDocId;
    }

    public Integer getMatDocRid() {
        return matDocRid;
    }

    public void setMatDocRid(Integer matDocRid) {
        this.matDocRid = matDocRid;
    }

    public Integer getMatDocYear() {
        return matDocYear;
    }

    public void setMatDocYear(Integer matDocYear) {
        this.matDocYear = matDocYear;
    }

   

    public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
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
}
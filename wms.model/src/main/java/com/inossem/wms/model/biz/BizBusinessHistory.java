package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizBusinessHistory {
    private Integer businessHistoryId;

    private Integer referReceiptId;

    private String referReceiptCode;

    private Integer referReceiptRid;

    private Integer referReceiptPid;

    private Byte businessType;

    private Byte receiptType;

    private Integer matId;

    private Long batch;

    private Integer ftyId;

    private Integer locationId;

    private Integer areaId;

    private Integer positionId;

    private Integer palletId;

    private Integer packageId;

    private BigDecimal qty;

    private String debitCredit;

    private Boolean synSap;

    private Boolean synMes;

    private String createUser;

    private Date createTime;

    private String modifyUser;

    private Date modifyTime;
    
    
    public Integer getBusinessHistoryId() {
        return businessHistoryId;
    }

    public void setBusinessHistoryId(Integer businessHistoryId) {
        this.businessHistoryId = businessHistoryId;
    }

    public Integer getReferReceiptId() {
		return referReceiptId;
	}

	public void setReferReceiptId(Integer referReceiptId) {
        this.referReceiptId = referReceiptId;
    }

    public String getReferReceiptCode() {
        return referReceiptCode;
    }

    public void setReferReceiptCode(String referReceiptCode) {
        this.referReceiptCode = referReceiptCode == null ? null : referReceiptCode.trim();
    }

    public Integer getReferReceiptRid() {
        return referReceiptRid;
    }

    public void setReferReceiptRid(Integer referReceiptRid) {
        this.referReceiptRid = referReceiptRid;
    }

    public Integer getReferReceiptPid() {
        return referReceiptPid;
    }

    public void setReferReceiptPid(Integer referReceiptPid) {
        this.referReceiptPid = referReceiptPid;
    }

    public Byte getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Byte businessType) {
        this.businessType = businessType;
    }

    public Byte getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Byte receiptType) {
        this.receiptType = receiptType;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getPalletId() {
        return palletId;
    }

    public void setPalletId(Integer palletId) {
        this.palletId = palletId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getDebitCredit() {
        return debitCredit;
    }

    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit == null ? null : debitCredit.trim();
    }

    public Boolean getSynSap() {
        return synSap;
    }

    public void setSynSap(Boolean synSap) {
        this.synSap = synSap;
    }

    public Boolean getSynMes() {
        return synMes;
    }

    public void setSynMes(Boolean synMes) {
        this.synMes = synMes;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    
    
}
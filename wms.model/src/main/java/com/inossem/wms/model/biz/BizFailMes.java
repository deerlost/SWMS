package com.inossem.wms.model.biz;

import java.util.Date;

public class BizFailMes {
    private Integer failId;

    private Integer referReceiptId;

    private String referReceiptCode;

    private Byte referReceiptType;

    private String businessType;

    private String createUser;

    private Date createTime;

    private String modifyUser;

    private Date modifyTime;

    private Byte status;

    private String errorInfo;

    public Integer getFailId() {
        return failId;
    }

    public void setFailId(Integer failId) {
        this.failId = failId;
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

    public Byte getReferReceiptType() {
        return referReceiptType;
    }

    public void setReferReceiptType(Byte referReceiptType) {
        this.referReceiptType = referReceiptType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo == null ? null : errorInfo.trim();
    }
}
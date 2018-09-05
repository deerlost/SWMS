package com.inossem.wms.model.log;

import java.util.Date;

public class LogReceiptUser {
    private Integer id;

    private String matDocCode;

    private String receiptCode;

    private String firstUserName;

    private Date firstTime;

    private String secondUserName;

    private Date secondTime;

    private String extraUserName;

    private Date extraTime;

    private String moveTypeCode;

    private String createUserName;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatDocCode() {
        return matDocCode;
    }

    public void setMatDocCode(String matDocCode) {
        this.matDocCode = matDocCode == null ? null : matDocCode.trim();
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode == null ? null : receiptCode.trim();
    }

    public String getFirstUserName() {
        return firstUserName;
    }

    public void setFirstUserName(String firstUserName) {
        this.firstUserName = firstUserName == null ? null : firstUserName.trim();
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public String getSecondUserName() {
        return secondUserName;
    }

    public void setSecondUserName(String secondUserName) {
        this.secondUserName = secondUserName == null ? null : secondUserName.trim();
    }

    public Date getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(Date secondTime) {
        this.secondTime = secondTime;
    }

    public String getExtraUserName() {
        return extraUserName;
    }

    public void setExtraUserName(String extraUserName) {
        this.extraUserName = extraUserName == null ? null : extraUserName.trim();
    }

    public Date getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(Date extraTime) {
        this.extraTime = extraTime;
    }

    public String getMoveTypeCode() {
        return moveTypeCode;
    }

    public void setMoveTypeCode(String moveTypeCode) {
        this.moveTypeCode = moveTypeCode == null ? null : moveTypeCode.trim();
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
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
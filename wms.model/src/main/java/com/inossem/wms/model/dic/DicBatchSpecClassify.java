package com.inossem.wms.model.dic;

import java.util.Date;

public class DicBatchSpecClassify {
    private Integer batchSpecClassifyId;

    private String batchSpecClassifyCode;

    private String batchSpecClassifyName;

    private String batchSpecClassifyType;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getBatchSpecClassifyId() {
        return batchSpecClassifyId;
    }

    public void setBatchSpecClassifyId(Integer batchSpecClassifyId) {
        this.batchSpecClassifyId = batchSpecClassifyId;
    }

    public String getBatchSpecClassifyCode() {
        return batchSpecClassifyCode;
    }

    public void setBatchSpecClassifyCode(String batchSpecClassifyCode) {
        this.batchSpecClassifyCode = batchSpecClassifyCode == null ? null : batchSpecClassifyCode.trim();
    }

    public String getBatchSpecClassifyName() {
        return batchSpecClassifyName;
    }

    public void setBatchSpecClassifyName(String batchSpecClassifyName) {
        this.batchSpecClassifyName = batchSpecClassifyName == null ? null : batchSpecClassifyName.trim();
    }

    public String getBatchSpecClassifyType() {
        return batchSpecClassifyType;
    }

    public void setBatchSpecClassifyType(String batchSpecClassifyType) {
        this.batchSpecClassifyType = batchSpecClassifyType == null ? null : batchSpecClassifyType.trim();
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
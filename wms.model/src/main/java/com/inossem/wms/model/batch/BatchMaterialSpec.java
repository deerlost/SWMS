package com.inossem.wms.model.batch;

import java.util.Date;

public class BatchMaterialSpec {
    private Integer id;

    private Integer matId;

    private Integer ftyId;

    private Long batch;

    private String batchSpecCode;

    private String batchSpecValue;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public String getBatchSpecCode() {
        return batchSpecCode;
    }

    public void setBatchSpecCode(String batchSpecCode) {
        this.batchSpecCode = batchSpecCode == null ? null : batchSpecCode.trim();
    }

    public String getBatchSpecValue() {
        return batchSpecValue;
    }

    public void setBatchSpecValue(String batchSpecValue) {
        this.batchSpecValue = batchSpecValue == null ? null : batchSpecValue.trim();
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
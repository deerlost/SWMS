package com.inossem.wms.model.dic;

import java.util.Date;

public class DicBatchSpec {
	//---------satrt--------------
	private String batchSpecValue;
	private Byte edit;
	
	//----------end--------------
	
    private Integer batchSpecId;

    public Byte getEdit() {
		return edit;
	}

	public void setEdit(Byte edit) {
		this.edit = edit;
	}

	public String getBatchSpecValue() {
		return batchSpecValue;
	}

	public void setBatchSpecValue(String batchSpecValue) {
		this.batchSpecValue = batchSpecValue;
	}

	private String batchSpecCode;

    private String batchSpecName;

    private Byte batchSpecType;

    private Byte required;

    private String info;

    private Integer displayIndex;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getBatchSpecId() {
        return batchSpecId;
    }

    public void setBatchSpecId(Integer batchSpecId) {
        this.batchSpecId = batchSpecId;
    }

    public String getBatchSpecCode() {
        return batchSpecCode;
    }

    public void setBatchSpecCode(String batchSpecCode) {
        this.batchSpecCode = batchSpecCode == null ? null : batchSpecCode.trim();
    }

    public String getBatchSpecName() {
        return batchSpecName;
    }

    public void setBatchSpecName(String batchSpecName) {
        this.batchSpecName = batchSpecName == null ? null : batchSpecName.trim();
    }

    public Byte getBatchSpecType() {
        return batchSpecType;
    }

    public void setBatchSpecType(Byte batchSpecType) {
        this.batchSpecType = batchSpecType;
    }

    public Byte getRequired() {
        return required;
    }

    public void setRequired(Byte required) {
        this.required = required;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public Integer getDisplayIndex() {
        return displayIndex;
    }

    public void setDisplayIndex(Integer displayIndex) {
        this.displayIndex = displayIndex;
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
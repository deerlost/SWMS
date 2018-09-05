package com.inossem.wms.model.biz;

import java.util.Date;

public class BizPkgOperationHead {
    private Integer pkgOperationId;
    private String pkgOperationCode;
    private Byte referReceiptType;
    private Integer referReceiptId;
    private String referReceiptCode;
    private String packageTeam;
    private Integer productLine;
    private Integer classTypeId;
    private Integer installation;
    private Byte synType;
    private String createUser;
    private Byte isDelete;
    private Date createTime;
    private Date modifyTime;
    private Byte status;
    private String remark;
    private String productBatch;
    private String documentType;
    private Byte isPallet;
    private Integer beforeOrderId;
    private String beforeOrderCode;
    private Byte beforeOrderType;
    private Integer beforeOrderRid;

 
    public Integer getBeforeOrderId() {
        return beforeOrderId;
    }

    public void setBeforeOrderId(Integer beforeOrderId) {
        this.beforeOrderId = beforeOrderId;
    }

    public String getBeforeOrderCode() {
        return beforeOrderCode;
    }

    public void setBeforeOrderCode(String beforeOrderCode) {
        this.beforeOrderCode = beforeOrderCode;
    }

    public Byte getBeforeOrderType() {
        return beforeOrderType;
    }

    public void setBeforeOrderType(Byte beforeOrderType) {
        this.beforeOrderType = beforeOrderType;
    }

    public Integer getBeforeOrderRid() {
        return beforeOrderRid;
    }

    public void setBeforeOrderRid(Integer beforeOrderRid) {
        this.beforeOrderRid = beforeOrderRid;
    }

    public Byte getIsPallet() {
		return isPallet;
	}

	public void setIsPallet(Byte isPallet) {
		this.isPallet = isPallet;
	}

	public Integer getPkgOperationId() {
        return this.pkgOperationId;
    }

    public void setPkgOperationId(Integer pkgOperationId) {
        this.pkgOperationId = pkgOperationId;
    }

    public String getPkgOperationCode() {
        return this.pkgOperationCode;
    }

    public void setPkgOperationCode(String pkgOperationCode) {
        this.pkgOperationCode = pkgOperationCode == null?null:pkgOperationCode.trim();
    }

    public Byte getReferReceiptType() {
        return this.referReceiptType;
    }

    public void setReferReceiptType(Byte referReceiptType) {
        this.referReceiptType = referReceiptType;
    }

    public Integer getReferReceiptId() {
        return this.referReceiptId;
    }

    public void setReferReceiptId(Integer referReceiptId) {
        this.referReceiptId = referReceiptId;
    }

    public String getReferReceiptCode() {
        return this.referReceiptCode;
    }

    public void setReferReceiptCode(String referReceiptCode) {
        this.referReceiptCode = referReceiptCode == null?null:referReceiptCode.trim();
    }

    public String getPackageTeam() {
        return this.packageTeam;
    }

    public void setPackageTeam(String packageTeam) {
        this.packageTeam = packageTeam == null?null:packageTeam.trim();
    }

    public Integer getProductLine() {
        return this.productLine;
    }

    public void setProductLine(Integer productLine) {
        this.productLine = productLine;
    }

    public Integer getClassTypeId() {
        return this.classTypeId;
    }

    public void setClassTypeId(Integer classTypeId) {
        this.classTypeId = classTypeId;
    }

    public Integer getInstallation() {
        return this.installation;
    }

    public void setInstallation(Integer installation) {
        this.installation = installation;
    }

    public Byte getSynType() {
        return this.synType;
    }

    public void setSynType(Byte synType) {
        this.synType = synType;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null?null:createUser.trim();
    }

    public Byte getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null?null:remark.trim();
    }

    public String getProductBatch() {
        return this.productBatch;
    }

    public void setProductBatch(String productBatch) {
        this.productBatch = productBatch == null?null:productBatch.trim();
    }

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}


}

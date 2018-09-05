package com.inossem.wms.model.syn;

import java.util.Date;

public class SynInspectNoticeHead {
    private String inspectNoticeCode;

    private String corpCode;

    private String corpName;

    private String purchaseOrderCode;

    private String purchaseOrderType;

    private String purchaseOrderTypeName;

    private String supplierCode;

    private String supplierName;

    private String createUser;

    private String deptCode;

    private String deptName;

    private String contractCode;

    private String contractName;

    private String supplierContact;

    private String supplierContactTel;

    private Byte isDirectSupply;

    private Byte status;

    private String remark;

    private Integer deliveryNoticeId;

    private Integer inspectNoticeId;

    private Date createTime;

    private Date modifyTime;

    public String getInspectNoticeCode() {
        return inspectNoticeCode;
    }

    public void setInspectNoticeCode(String inspectNoticeCode) {
        this.inspectNoticeCode = inspectNoticeCode == null ? null : inspectNoticeCode.trim();
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode == null ? null : corpCode.trim();
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName == null ? null : corpName.trim();
    }

    public String getPurchaseOrderCode() {
        return purchaseOrderCode;
    }

    public void setPurchaseOrderCode(String purchaseOrderCode) {
        this.purchaseOrderCode = purchaseOrderCode == null ? null : purchaseOrderCode.trim();
    }

    public String getPurchaseOrderType() {
        return purchaseOrderType;
    }

    public void setPurchaseOrderType(String purchaseOrderType) {
        this.purchaseOrderType = purchaseOrderType == null ? null : purchaseOrderType.trim();
    }

    public String getPurchaseOrderTypeName() {
        return purchaseOrderTypeName;
    }

    public void setPurchaseOrderTypeName(String purchaseOrderTypeName) {
        this.purchaseOrderTypeName = purchaseOrderTypeName == null ? null : purchaseOrderTypeName.trim();
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode == null ? null : deptCode.trim();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact == null ? null : supplierContact.trim();
    }

    public String getSupplierContactTel() {
        return supplierContactTel;
    }

    public void setSupplierContactTel(String supplierContactTel) {
        this.supplierContactTel = supplierContactTel == null ? null : supplierContactTel.trim();
    }

    public Byte getIsDirectSupply() {
        return isDirectSupply;
    }

    public void setIsDirectSupply(Byte isDirectSupply) {
        this.isDirectSupply = isDirectSupply;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getDeliveryNoticeId() {
        return deliveryNoticeId;
    }

    public void setDeliveryNoticeId(Integer deliveryNoticeId) {
        this.deliveryNoticeId = deliveryNoticeId;
    }

    public Integer getInspectNoticeId() {
        return inspectNoticeId;
    }

    public void setInspectNoticeId(Integer inspectNoticeId) {
        this.inspectNoticeId = inspectNoticeId;
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
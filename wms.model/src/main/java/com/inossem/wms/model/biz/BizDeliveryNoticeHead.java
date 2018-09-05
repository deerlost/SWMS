package com.inossem.wms.model.biz;

import java.util.Date;

public class BizDeliveryNoticeHead {
    private Integer deliveryNoticeId;

    private String deliveryNoticeCode;

    private Integer corpId;

    private String purchaseOrderCode;

    private String purchaseOrderType;

    private String purchaseOrderTypeName;

    private String supplierCode;

    private String supplierName;

    private String deptCode;

    private String deptName;

    private String contractCode;

    private String contractName;

    private String supplierContact;

    private String supplierContactTel;

    private Byte isDirectSupply;

    private Byte status;

    private String remark;

    private Date deliveryDate;

    private Byte isDistribute;

    private String purchaseOrg;

    private String purchaseOrgName;

    private String purchaseGroup;

    private String purchaseGroupName;

    private String purchaseSpecialty;

    private String modifyUser;

    private String createUser;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getDeliveryNoticeId() {
        return deliveryNoticeId;
    }

    public void setDeliveryNoticeId(Integer deliveryNoticeId) {
        this.deliveryNoticeId = deliveryNoticeId;
    }

    public String getDeliveryNoticeCode() {
        return deliveryNoticeCode;
    }

    public void setDeliveryNoticeCode(String deliveryNoticeCode) {
        this.deliveryNoticeCode = deliveryNoticeCode == null ? null : deliveryNoticeCode.trim();
    }

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Byte getIsDistribute() {
        return isDistribute;
    }

    public void setIsDistribute(Byte isDistribute) {
        this.isDistribute = isDistribute;
    }

    public String getPurchaseOrg() {
        return purchaseOrg;
    }

    public void setPurchaseOrg(String purchaseOrg) {
        this.purchaseOrg = purchaseOrg == null ? null : purchaseOrg.trim();
    }

    public String getPurchaseOrgName() {
        return purchaseOrgName;
    }

    public void setPurchaseOrgName(String purchaseOrgName) {
        this.purchaseOrgName = purchaseOrgName == null ? null : purchaseOrgName.trim();
    }

    public String getPurchaseGroup() {
        return purchaseGroup;
    }

    public void setPurchaseGroup(String purchaseGroup) {
        this.purchaseGroup = purchaseGroup == null ? null : purchaseGroup.trim();
    }

    public String getPurchaseGroupName() {
        return purchaseGroupName;
    }

    public void setPurchaseGroupName(String purchaseGroupName) {
        this.purchaseGroupName = purchaseGroupName == null ? null : purchaseGroupName.trim();
    }

    public String getPurchaseSpecialty() {
        return purchaseSpecialty;
    }

    public void setPurchaseSpecialty(String purchaseSpecialty) {
        this.purchaseSpecialty = purchaseSpecialty == null ? null : purchaseSpecialty.trim();
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
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
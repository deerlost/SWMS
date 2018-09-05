package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.util.Date;

public class BizInspectNoticeItem {
    private Integer itemId;

    private Integer inspectNoticeId;

    private Integer inspectNoticeRid;

    private Integer matId;

    private BigDecimal arriveQty;

    private BigDecimal qualifiedQty;

    private BigDecimal unqualifiedQty;

    private Integer unitId;

    private String manufacturer;

    private String contactAccount;

    private String contact;

    private String contactTel;

    private String inspectAddress;

    private String useUnit;

    private String matUse;

    private String inspectorAccount;

    private String inspector;

    private String inspectorTel;

    private String purchaseOrderCode;

    private String purchaseOrderRid;

    private Integer deliveryNoticeId;

    private Integer deliveryNoticeRid;

    private String exfactoryCode;

    private String remark;

    private BigDecimal orderQty;

    private BigDecimal deliveryQty;

    private String contractCode;

    private Date arrivalTime;

    private Date actualArrivalTime;

    private Byte unqualifiedMethod;

    private String modifyUser;

    private BigDecimal inputedQty;

    private Integer ftyId;

    private Integer locationId;

    private String specStock;

    private String specStockCode;

    private String specStockName;

    private Byte decimalPlace;

    private Date planDeliveredDate;

    private String demandDept;

    private String costObjCode;

    private String costObjName;

    private String useDeptCode;

    private Date createTime;

    private Byte isDelete;

    private Date modifyTime;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getInspectNoticeId() {
        return inspectNoticeId;
    }

    public void setInspectNoticeId(Integer inspectNoticeId) {
        this.inspectNoticeId = inspectNoticeId;
    }

    public Integer getInspectNoticeRid() {
        return inspectNoticeRid;
    }

    public void setInspectNoticeRid(Integer inspectNoticeRid) {
        this.inspectNoticeRid = inspectNoticeRid;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public BigDecimal getArriveQty() {
        return arriveQty;
    }

    public void setArriveQty(BigDecimal arriveQty) {
        this.arriveQty = arriveQty;
    }

    public BigDecimal getQualifiedQty() {
        return qualifiedQty;
    }

    public void setQualifiedQty(BigDecimal qualifiedQty) {
        this.qualifiedQty = qualifiedQty;
    }

    public BigDecimal getUnqualifiedQty() {
        return unqualifiedQty;
    }

    public void setUnqualifiedQty(BigDecimal unqualifiedQty) {
        this.unqualifiedQty = unqualifiedQty;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public String getContactAccount() {
        return contactAccount;
    }

    public void setContactAccount(String contactAccount) {
        this.contactAccount = contactAccount == null ? null : contactAccount.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel == null ? null : contactTel.trim();
    }

    public String getInspectAddress() {
        return inspectAddress;
    }

    public void setInspectAddress(String inspectAddress) {
        this.inspectAddress = inspectAddress == null ? null : inspectAddress.trim();
    }

    public String getUseUnit() {
        return useUnit;
    }

    public void setUseUnit(String useUnit) {
        this.useUnit = useUnit == null ? null : useUnit.trim();
    }

    public String getMatUse() {
        return matUse;
    }

    public void setMatUse(String matUse) {
        this.matUse = matUse == null ? null : matUse.trim();
    }

    public String getInspectorAccount() {
        return inspectorAccount;
    }

    public void setInspectorAccount(String inspectorAccount) {
        this.inspectorAccount = inspectorAccount == null ? null : inspectorAccount.trim();
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector == null ? null : inspector.trim();
    }

    public String getInspectorTel() {
        return inspectorTel;
    }

    public void setInspectorTel(String inspectorTel) {
        this.inspectorTel = inspectorTel == null ? null : inspectorTel.trim();
    }

    public String getPurchaseOrderCode() {
        return purchaseOrderCode;
    }

    public void setPurchaseOrderCode(String purchaseOrderCode) {
        this.purchaseOrderCode = purchaseOrderCode == null ? null : purchaseOrderCode.trim();
    }

    public String getPurchaseOrderRid() {
        return purchaseOrderRid;
    }

    public void setPurchaseOrderRid(String purchaseOrderRid) {
        this.purchaseOrderRid = purchaseOrderRid == null ? null : purchaseOrderRid.trim();
    }

    public Integer getDeliveryNoticeId() {
        return deliveryNoticeId;
    }

    public void setDeliveryNoticeId(Integer deliveryNoticeId) {
        this.deliveryNoticeId = deliveryNoticeId;
    }

    public Integer getDeliveryNoticeRid() {
        return deliveryNoticeRid;
    }

    public void setDeliveryNoticeRid(Integer deliveryNoticeRid) {
        this.deliveryNoticeRid = deliveryNoticeRid;
    }

    public String getExfactoryCode() {
        return exfactoryCode;
    }

    public void setExfactoryCode(String exfactoryCode) {
        this.exfactoryCode = exfactoryCode == null ? null : exfactoryCode.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getDeliveryQty() {
        return deliveryQty;
    }

    public void setDeliveryQty(BigDecimal deliveryQty) {
        this.deliveryQty = deliveryQty;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(Date actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public Byte getUnqualifiedMethod() {
        return unqualifiedMethod;
    }

    public void setUnqualifiedMethod(Byte unqualifiedMethod) {
        this.unqualifiedMethod = unqualifiedMethod;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }

    public BigDecimal getInputedQty() {
        return inputedQty;
    }

    public void setInputedQty(BigDecimal inputedQty) {
        this.inputedQty = inputedQty;
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

    public String getSpecStock() {
        return specStock;
    }

    public void setSpecStock(String specStock) {
        this.specStock = specStock == null ? null : specStock.trim();
    }

    public String getSpecStockCode() {
        return specStockCode;
    }

    public void setSpecStockCode(String specStockCode) {
        this.specStockCode = specStockCode == null ? null : specStockCode.trim();
    }

    public String getSpecStockName() {
        return specStockName;
    }

    public void setSpecStockName(String specStockName) {
        this.specStockName = specStockName == null ? null : specStockName.trim();
    }

    public Byte getDecimalPlace() {
        return decimalPlace;
    }

    public void setDecimalPlace(Byte decimalPlace) {
        this.decimalPlace = decimalPlace;
    }

    public Date getPlanDeliveredDate() {
        return planDeliveredDate;
    }

    public void setPlanDeliveredDate(Date planDeliveredDate) {
        this.planDeliveredDate = planDeliveredDate;
    }

    public String getDemandDept() {
        return demandDept;
    }

    public void setDemandDept(String demandDept) {
        this.demandDept = demandDept == null ? null : demandDept.trim();
    }

    public String getCostObjCode() {
        return costObjCode;
    }

    public void setCostObjCode(String costObjCode) {
        this.costObjCode = costObjCode == null ? null : costObjCode.trim();
    }

    public String getCostObjName() {
        return costObjName;
    }

    public void setCostObjName(String costObjName) {
        this.costObjName = costObjName == null ? null : costObjName.trim();
    }

    public String getUseDeptCode() {
        return useDeptCode;
    }

    public void setUseDeptCode(String useDeptCode) {
        this.useDeptCode = useDeptCode == null ? null : useDeptCode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
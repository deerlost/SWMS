package com.inossem.wms.model.syn;

import java.math.BigDecimal;

public class SynInspectNoticeItem {
    private Integer itemId;

    private String inspectNoticeCode;

    private String inspectRid;

    private Integer matId;

    private BigDecimal arriveQty;

    private BigDecimal takeDeliveryQty;

    private BigDecimal inspectQty;

    private Integer unitId;

    private Byte decimalPlace;

    private String purchaseOrderCode;

    private String purchaseOrderRid;

    private String manufacturer;

    private String contactAccount;

    private String contact;

    private String contactTel;

    private Integer ftyId;

    private String werks;

    private Integer locationId;

    private String lgort;

    private String inspectAddress;

    private String useDeptCode;

    private String matUse;

    private String inspectorAccount;

    private String inspector;

    private String inspectorTel;

    private String specStock;

    private String specStockCode;

    private String specStockName;

    private Integer inspectNoticeId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getInspectNoticeCode() {
        return inspectNoticeCode;
    }

    public void setInspectNoticeCode(String inspectNoticeCode) {
        this.inspectNoticeCode = inspectNoticeCode == null ? null : inspectNoticeCode.trim();
    }

    public String getInspectRid() {
        return inspectRid;
    }

    public void setInspectRid(String inspectRid) {
        this.inspectRid = inspectRid == null ? null : inspectRid.trim();
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

    public BigDecimal getTakeDeliveryQty() {
        return takeDeliveryQty;
    }

    public void setTakeDeliveryQty(BigDecimal takeDeliveryQty) {
        this.takeDeliveryQty = takeDeliveryQty;
    }

    public BigDecimal getInspectQty() {
        return inspectQty;
    }

    public void setInspectQty(BigDecimal inspectQty) {
        this.inspectQty = inspectQty;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Byte getDecimalPlace() {
        return decimalPlace;
    }

    public void setDecimalPlace(Byte decimalPlace) {
        this.decimalPlace = decimalPlace;
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

    public Integer getFtyId() {
        return ftyId;
    }

    public void setFtyId(Integer ftyId) {
        this.ftyId = ftyId;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks == null ? null : werks.trim();
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort == null ? null : lgort.trim();
    }

    public String getInspectAddress() {
        return inspectAddress;
    }

    public void setInspectAddress(String inspectAddress) {
        this.inspectAddress = inspectAddress == null ? null : inspectAddress.trim();
    }

    public String getUseDeptCode() {
        return useDeptCode;
    }

    public void setUseDeptCode(String useDeptCode) {
        this.useDeptCode = useDeptCode == null ? null : useDeptCode.trim();
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

    public Integer getInspectNoticeId() {
        return inspectNoticeId;
    }

    public void setInspectNoticeId(Integer inspectNoticeId) {
        this.inspectNoticeId = inspectNoticeId;
    }
}
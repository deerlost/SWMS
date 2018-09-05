package com.inossem.wms.model.biz;

import java.util.Date;

public class BizAllocateDeliveryHead {
    private Integer allocateDeliveryId;

    private String allocateDeliveryCode;

    private Integer corpId;

    private String createUser;

    private Date deliveryDate;

    private String deliveryVehicle;

    private String deliveryDriver;

    private String deliveryPhone;

    private Integer ftyOutput;

    private Integer locationOutput;

    private String remark;

    private Byte status;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getAllocateDeliveryId() {
        return allocateDeliveryId;
    }

    public void setAllocateDeliveryId(Integer allocateDeliveryId) {
        this.allocateDeliveryId = allocateDeliveryId;
    }

    public String getAllocateDeliveryCode() {
        return allocateDeliveryCode;
    }

    public void setAllocateDeliveryCode(String allocateDeliveryCode) {
        this.allocateDeliveryCode = allocateDeliveryCode == null ? null : allocateDeliveryCode.trim();
    }

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryVehicle() {
        return deliveryVehicle;
    }

    public void setDeliveryVehicle(String deliveryVehicle) {
        this.deliveryVehicle = deliveryVehicle == null ? null : deliveryVehicle.trim();
    }

    public String getDeliveryDriver() {
        return deliveryDriver;
    }

    public void setDeliveryDriver(String deliveryDriver) {
        this.deliveryDriver = deliveryDriver == null ? null : deliveryDriver.trim();
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone == null ? null : deliveryPhone.trim();
    }

    public Integer getFtyOutput() {
        return ftyOutput;
    }

    public void setFtyOutput(Integer ftyOutput) {
        this.ftyOutput = ftyOutput;
    }

    public Integer getLocationOutput() {
        return locationOutput;
    }

    public void setLocationOutput(Integer locationOutput) {
        this.locationOutput = locationOutput;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
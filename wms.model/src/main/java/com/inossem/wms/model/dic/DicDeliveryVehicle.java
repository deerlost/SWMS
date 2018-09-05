package com.inossem.wms.model.dic;

public class DicDeliveryVehicle {
    private Integer deliveryVehicleId;

    private String deliveryVehicleName;

    private Integer productLineId;

    private Byte isDelete;

    public Integer getDeliveryVehicleId() {
        return deliveryVehicleId;
    }

    public void setDeliveryVehicleId(Integer deliveryVehicleId) {
        this.deliveryVehicleId = deliveryVehicleId;
    }

    public String getDeliveryVehicleName() {
        return deliveryVehicleName;
    }

    public void setDeliveryVehicleName(String deliveryVehicleName) {
        this.deliveryVehicleName = deliveryVehicleName == null ? null : deliveryVehicleName.trim();
    }

    public Integer getProductLineId() {
        return productLineId;
    }

    public void setProductLineId(Integer productLineId) {
        this.productLineId = productLineId;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
package com.inossem.wms.model.dic;

public class DicDeliveryDriver {
    private Integer deliveryDriverId;

    private String deliveryDriverName;

    private Integer productLineId;

    private Byte isDelete;

    public Integer getDeliveryDriverId() {
        return deliveryDriverId;
    }

    public void setDeliveryDriverId(Integer deliveryDriverId) {
        this.deliveryDriverId = deliveryDriverId;
    }

    public String getDeliveryDriverName() {
        return deliveryDriverName;
    }

    public void setDeliveryDriverName(String deliveryDriverName) {
        this.deliveryDriverName = deliveryDriverName == null ? null : deliveryDriverName.trim();
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
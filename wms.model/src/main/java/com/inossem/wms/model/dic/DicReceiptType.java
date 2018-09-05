package com.inossem.wms.model.dic;

public class DicReceiptType {
    private Byte receiptType;

    private String receiptTypeName;

    public Byte getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Byte receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptTypeName() {
        return receiptTypeName;
    }

    public void setReceiptTypeName(String receiptTypeName) {
        this.receiptTypeName = receiptTypeName == null ? null : receiptTypeName.trim();
    }
}
package com.inossem.wms.model.dic;

import com.inossem.wms.model.page.PageCommon;

public class DicSupplierPackageType extends PageCommon{
    private Integer dicSupplierPackageTypeId;

    private Integer packageTypeId;

    private Integer dicSupplierId;

    private String supplierTimeStar;

    private String supplierTimeEnd;

    private Byte isFreeze;

    private Byte isDelete;

    public Integer getDicSupplierPackageTypeId() {
        return dicSupplierPackageTypeId;
    }

    public void setDicSupplierPackageTypeId(Integer dicSupplierPackageTypeId) {
        this.dicSupplierPackageTypeId = dicSupplierPackageTypeId;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public Integer getDicSupplierId() {
        return dicSupplierId;
    }

    public void setDicSupplierId(Integer dicSupplierId) {
        this.dicSupplierId = dicSupplierId;
    }

    public String getSupplierTimeStar() {
        return supplierTimeStar;
    }

    public void setSupplierTimeStar(String supplierTimeStar) {
        this.supplierTimeStar = supplierTimeStar == null ? null : supplierTimeStar.trim();
    }

    public String getSupplierTimeEnd() {
        return supplierTimeEnd;
    }

    public void setSupplierTimeEnd(String supplierTimeEnd) {
        this.supplierTimeEnd = supplierTimeEnd == null ? null : supplierTimeEnd.trim();
    }

    public Byte getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(Byte isFreeze) {
        this.isFreeze = isFreeze;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
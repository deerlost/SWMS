package com.inossem.wms.model.biz;

import java.util.Date;

public class BizPkgOperationPosition {
    private Integer itemPositionId;
    private Integer pkgOperationId;
    private Integer pkgOperationRid;
    private Integer pkgOperationPositionId;
    private Integer palletId;
    private Integer packageId;
    private Byte isDelete;
    private Date createTime;
    private Date modifyTime;
    private String packageCode;
    private Integer packageTypeId;
    
    
    public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public Integer getItemPositionId() {
        return this.itemPositionId;
    }

    public void setItemPositionId(Integer itemPositionId) {
        this.itemPositionId = itemPositionId;
    }

    public Integer getPkgOperationId() {
        return this.pkgOperationId;
    }

    public void setPkgOperationId(Integer pkgOperationId) {
        this.pkgOperationId = pkgOperationId;
    }

    public Integer getPkgOperationRid() {
        return this.pkgOperationRid;
    }

    public void setPkgOperationRid(Integer pkgOperationRid) {
        this.pkgOperationRid = pkgOperationRid;
    }

    public Integer getPkgOperationPositionId() {
        return this.pkgOperationPositionId;
    }

    public void setPkgOperationPositionId(Integer pkgOperationPositionId) {
        this.pkgOperationPositionId = pkgOperationPositionId;
    }

    public Integer getPalletId() {
        return this.palletId;
    }

    public void setPalletId(Integer palletId) {
        this.palletId = palletId;
    }

    public Integer getPackageId() {
        return this.packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
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
}

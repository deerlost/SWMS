package com.inossem.wms.model.serial;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialPackage {
	
    private Integer packageId;

    private String packageCode;
    
    private Integer matId;

    private Integer packageTypeId;

    private Date createTime;

    private String createUser;
    
    private Date modifyTime;
    
    private String modifyUser;
    
    private Byte isDelete;
    
    private Byte status;
    
    private Integer supplierId;
    
    public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode == null ? null : packageCode.trim();
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public String getCreateTime() {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (createTime != null) {
			return format.format(this.createTime);
		} else {
			return null;
		}
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (modifyTime != null) {
			return format.format(this.modifyTime);
		} else {
			return null;
		}
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
package com.inossem.wms.model.biz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BizDefinedReport {
    private Integer definedId;

    private String definedCode;

    private String definedName;

    private Byte definedType;

    private Date createTime;

    private Date modifyTime;

    private String createUser;

    private String modifyUser;

    private Byte isDelete;

    public Integer getDefinedId() {
        return definedId;
    }

    public void setDefinedId(Integer definedId) {
        this.definedId = definedId;
    }

    public String getDefinedCode() {
        return definedCode;
    }

    public void setDefinedCode(String definedCode) {
        this.definedCode = definedCode == null ? null : definedCode.trim();
    }

    public String getDefinedName() {
        return definedName;
    }

    public void setDefinedName(String definedName) {
        this.definedName = definedName == null ? null : definedName.trim();
    }

    public Byte getDefinedType() {
        return definedType;
    }

    public void setDefinedType(Byte definedType) {
        this.definedType = definedType;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
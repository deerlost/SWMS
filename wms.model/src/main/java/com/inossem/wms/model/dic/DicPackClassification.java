package com.inossem.wms.model.dic;

import java.util.List;

public class DicPackClassification {
    
	private Integer classificatId;

    private String classificatName;

    private Byte isDelete;
    
    private List<DicPackageType> packageTypeList;
   
	public List<DicPackageType> getPackageTypeList() {
		return packageTypeList;
	}

	public void setPackageTypeList(List<DicPackageType> packageTypeList) {
		this.packageTypeList = packageTypeList;
	}

	public Integer getClassificatId() {
        return classificatId;
    }

    public void setClassificatId(Integer classificatId) {
        this.classificatId = classificatId;
    }

    public String getClassificatName() {
        return classificatName;
    }

    public void setClassificatName(String classificatName) {
        this.classificatName = classificatName == null ? null : classificatName.trim();
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
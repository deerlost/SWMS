package com.inossem.wms.model.dic;

public class DicMaterialGroup {
    private Integer matGroupId;

    private String matGroupCode;

    private String matGroupName;

    private Integer batchSpecificClassifyId;

    private Byte matCheck;

    public Integer getMatGroupId() {
        return matGroupId;
    }

    public void setMatGroupId(Integer matGroupId) {
        this.matGroupId = matGroupId;
    }

    public String getMatGroupCode() {
        return matGroupCode;
    }

    public void setMatGroupCode(String matGroupCode) {
        this.matGroupCode = matGroupCode == null ? null : matGroupCode.trim();
    }

    public String getMatGroupName() {
        return matGroupName;
    }

    public void setMatGroupName(String matGroupName) {
        this.matGroupName = matGroupName == null ? null : matGroupName.trim();
    }

    public Integer getBatchSpecificClassifyId() {
        return batchSpecificClassifyId;
    }

    public void setBatchSpecificClassifyId(Integer batchSpecificClassifyId) {
        this.batchSpecificClassifyId = batchSpecificClassifyId;
    }

    public Byte getMatCheck() {
        return matCheck;
    }

    public void setMatCheck(Byte matCheck) {
        this.matCheck = matCheck;
    }
}
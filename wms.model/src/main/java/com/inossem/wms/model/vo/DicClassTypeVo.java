package com.inossem.wms.model.vo;

public class DicClassTypeVo {
    private Integer classTypeId;
    private String classTypeName;
    private String startTime;
    private String endTime;

    public DicClassTypeVo() {
    }

    public Integer getClassTypeId() {
        return this.classTypeId;
    }

    public void setClassTypeId(Integer classTypeId) {
        this.classTypeId = classTypeId;
    }

    public String getClassTypeName() {
        return this.classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

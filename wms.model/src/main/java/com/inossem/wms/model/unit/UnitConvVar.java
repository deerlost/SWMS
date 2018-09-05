package com.inossem.wms.model.unit;

public class UnitConvVar {
    private Integer id;

    private Integer unitConvId;

    private String name;

    private Integer unitId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitConvId() {
        return unitConvId;
    }

    public void setUnitConvId(Integer unitConvId) {
        this.unitConvId = unitConvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}
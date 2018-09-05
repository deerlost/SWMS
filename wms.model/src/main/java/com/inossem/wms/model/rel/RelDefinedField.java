package com.inossem.wms.model.rel;

public class RelDefinedField {
    private Integer id;

    private String fieldId;

    private Integer definedId;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId == null ? null : fieldId.trim();
    }

    public Integer getDefinedId() {
        return definedId;
    }

    public void setDefinedId(Integer definedId) {
        this.definedId = definedId;
    }
}
package com.inossem.wms.model.rel;

public class RelDefinedUser {
    private Integer id;

    private Integer definedId;

    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDefinedId() {
        return definedId;
    }

    public void setDefinedId(Integer definedId) {
        this.definedId = definedId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
}
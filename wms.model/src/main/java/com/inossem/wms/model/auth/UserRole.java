package com.inossem.wms.model.auth;

public class UserRole {
    private Integer userMapId;

    private String userId;

    private Integer roleId;

    public Integer getUserMapId() {
        return userMapId;
    }

    public void setUserMapId(Integer userMapId) {
        this.userMapId = userMapId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
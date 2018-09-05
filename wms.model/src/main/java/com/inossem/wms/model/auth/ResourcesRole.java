package com.inossem.wms.model.auth;

public class ResourcesRole {
    private Integer resourceMapId;

    private Integer roleId;

    private Integer resourcesId;

    public Integer getResourceMapId() {
        return resourceMapId;
    }

    public void setResourceMapId(Integer resourceMapId) {
        this.resourceMapId = resourceMapId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(Integer resourcesId) {
        this.resourcesId = resourcesId;
    }
}
package com.inossem.wms.model.auth;

import java.util.Date;

public class ORG {
    private String orgId;

    private String orgName;

    private String orgShortName;

    private String parentOrgId;

    private Long orgLevel;

    private String upperSupervisorid;

    private String upperSupervisorName;

    private String displayOrder;

    private String decription;

    private String dataSource;

    private Date createTime;

    private Date modifyTime;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getOrgShortName() {
        return orgShortName;
    }

    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName == null ? null : orgShortName.trim();
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId == null ? null : parentOrgId.trim();
    }

    public Long getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Long orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getUpperSupervisorid() {
        return upperSupervisorid;
    }

    public void setUpperSupervisorid(String upperSupervisorid) {
        this.upperSupervisorid = upperSupervisorid == null ? null : upperSupervisorid.trim();
    }

    public String getUpperSupervisorName() {
        return upperSupervisorName;
    }

    public void setUpperSupervisorName(String upperSupervisorName) {
        this.upperSupervisorName = upperSupervisorName == null ? null : upperSupervisorName.trim();
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder == null ? null : displayOrder.trim();
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription == null ? null : decription.trim();
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource == null ? null : dataSource.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
package com.inossem.wms.model.syn;

import java.util.Date;

public class SynMaterialDocHead {
    private Integer matDocId;

    private String matDocCode;

    private Integer matDocYear;

    private Date matDocTime;

    private Date postingTime;

    private Date createTime;

    private Date modifyTime;

    public Integer getMatDocId() {
        return matDocId;
    }

    public void setMatDocId(Integer matDocId) {
        this.matDocId = matDocId;
    }

    public String getMatDocCode() {
        return matDocCode;
    }

    public void setMatDocCode(String matDocCode) {
        this.matDocCode = matDocCode == null ? null : matDocCode.trim();
    }

    public Integer getMatDocYear() {
        return matDocYear;
    }

    public void setMatDocYear(Integer matDocYear) {
        this.matDocYear = matDocYear;
    }

    public Date getMatDocTime() {
        return matDocTime;
    }

    public void setMatDocTime(Date matDocTime) {
        this.matDocTime = matDocTime;
    }

    public Date getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(Date postingTime) {
        this.postingTime = postingTime;
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
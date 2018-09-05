package com.inossem.wms.model.dic;

public class DicMaterialReqBizType {
    private Integer matReqBizTypeId;

    private Integer boardId;

    private String bizTypeName;

    public Integer getMatReqBizTypeId() {
        return matReqBizTypeId;
    }

    public void setMatReqBizTypeId(Integer matReqBizTypeId) {
        this.matReqBizTypeId = matReqBizTypeId;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName == null ? null : bizTypeName.trim();
    }
}
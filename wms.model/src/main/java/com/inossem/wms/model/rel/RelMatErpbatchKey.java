package com.inossem.wms.model.rel;

public class RelMatErpbatchKey {
    private Integer matId;

    private String erpBatch;

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public String getErpBatch() {
        return erpBatch;
    }

    public void setErpBatch(String erpBatch) {
        this.erpBatch = erpBatch == null ? null : erpBatch.trim();
    }
}
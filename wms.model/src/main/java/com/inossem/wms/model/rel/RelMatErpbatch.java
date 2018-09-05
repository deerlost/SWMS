package com.inossem.wms.model.rel;

public class RelMatErpbatch extends RelMatErpbatchKey {
    private String matCode;

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode == null ? null : matCode.trim();
    }
}
package com.inossem.wms.model.buf;

import java.math.BigDecimal;

public class BufUserLocationMatStock {
    private Integer buffId;

    private Integer matId;

    private Integer locationId;

    private String erpBatch;

    private String userId;

    private BigDecimal startQty;

    private BigDecimal endDateInputQty;

    private BigDecimal endDateOutputQty;

    private BigDecimal betweenDateInputQty;

    private BigDecimal betweenDateOutputQty;

    public Integer getBuffId() {
        return buffId;
    }

    public void setBuffId(Integer buffId) {
        this.buffId = buffId;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getErpBatch() {
        return erpBatch;
    }

    public void setErpBatch(String erpBatch) {
        this.erpBatch = erpBatch == null ? null : erpBatch.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public BigDecimal getStartQty() {
        return startQty;
    }

    public void setStartQty(BigDecimal startQty) {
        this.startQty = startQty;
    }

    public BigDecimal getEndDateInputQty() {
        return endDateInputQty;
    }

    public void setEndDateInputQty(BigDecimal endDateInputQty) {
        this.endDateInputQty = endDateInputQty;
    }

    public BigDecimal getEndDateOutputQty() {
        return endDateOutputQty;
    }

    public void setEndDateOutputQty(BigDecimal endDateOutputQty) {
        this.endDateOutputQty = endDateOutputQty;
    }

    public BigDecimal getBetweenDateInputQty() {
        return betweenDateInputQty;
    }

    public void setBetweenDateInputQty(BigDecimal betweenDateInputQty) {
        this.betweenDateInputQty = betweenDateInputQty;
    }

    public BigDecimal getBetweenDateOutputQty() {
        return betweenDateOutputQty;
    }

    public void setBetweenDateOutputQty(BigDecimal betweenDateOutputQty) {
        this.betweenDateOutputQty = betweenDateOutputQty;
    }
}
package com.inossem.wms.model.vo;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTransportItem;

public class BizStockTransportItemVo extends BizStockTransportItem {

    private String stockTransportCode;
    private String stockOutputCode;

    public String getStockOutputCode() {
        return stockOutputCode;
    }

    public void setStockOutputCode(String stockOutputCode) {
        this.stockOutputCode = stockOutputCode;
    }

    private String ftyCode;

    private String ftyName;

    private String locationCode;

    private String locationName;

    private String matCode;

    private String matName;

    private String whCode;

    private String whName;

    private String areaCode;

    private String areaName;

    private String positionCode;

    private String unitCode;

    private String unitName;

    private String inputFtyCode;

    private String inputFtyName;

    private String inputLocationCode;

    private String inputLocationName;

    private String inputMatCode;

    private String inputMatName;

    private String snCode;

    private String packageCode;

    private List<Map<String, Object>> snList;


    public List<Map<String, Object>> getSnList() {
        return snList;
    }

    public void setSnList(List<Map<String, Object>> snList) {
        this.snList = snList;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public String getStockTransportCode() {
        return stockTransportCode;
    }

    public void setStockTransportCode(String stockTransportCode) {
        this.stockTransportCode = stockTransportCode;
    }

    public String getFtyCode() {
        return ftyCode;
    }

    public void setFtyCode(String ftyCode) {
        this.ftyCode = ftyCode;
    }

    public String getFtyName() {
        return ftyName;
    }

    public void setFtyName(String ftyName) {
        this.ftyName = ftyName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getInputFtyCode() {
        return inputFtyCode;
    }

    public void setInputFtyCode(String inputFtyCode) {
        this.inputFtyCode = inputFtyCode;
    }

    public String getInputFtyName() {
        return inputFtyName;
    }

    public void setInputFtyName(String inputFtyName) {
        this.inputFtyName = inputFtyName;
    }

    public String getInputLocationCode() {
        return inputLocationCode;
    }

    public void setInputLocationCode(String inputLocationCode) {
        this.inputLocationCode = inputLocationCode;
    }

    public String getInputLocationName() {
        return inputLocationName;
    }

    public void setInputLocationName(String inputLocationName) {
        this.inputLocationName = inputLocationName;
    }

    public String getInputMatCode() {
        return inputMatCode;
    }

    public void setInputMatCode(String inputMatCode) {
        this.inputMatCode = inputMatCode;
    }

    public String getInputMatName() {
        return inputMatName;
    }

    public void setInputMatName(String inputMatName) {
        this.inputMatName = inputMatName;
    }

}

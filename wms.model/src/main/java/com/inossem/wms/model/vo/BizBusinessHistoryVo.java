package com.inossem.wms.model.vo;

import java.math.BigDecimal;
import java.util.List;

import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class BizBusinessHistoryVo implements IPageCommon{
	
	//分页属性
	private boolean paging = false;
    private int pageSize;
    private int pageIndex;
    private int totalCount = -1;
    private boolean sortAscend;
    private String sortColumn;
    
	
	private Integer matId;
	private String matCode;
	private String matName;
	private Integer unitId;
	private String unitCode;
	private String unitName;
	private BigDecimal beginQty;
	private BigDecimal inputQty;
	private BigDecimal outputQty;
	private BigDecimal endQty;
	private Integer locationId;
	private String locationCode;
	private String locationName;
	private Integer ftyId;
	private String ftyCode;
	private String ftyName;
	private String erpBatch;
	private Integer packageTypeId;
	private String packageTypeName;
	private String packageTypeCode;
	private BigDecimal packageStandard;
	private String packageStandardCh;

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
	public Integer getPackageTypeId() {
		return packageTypeId;
	}
	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}
	public String getPackageTypeName() {
		return packageTypeName;
	}
	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName;
	}
	public String getPackageTypeCode() {
		return packageTypeCode;
	}
	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
	}
	public BigDecimal getPackageStandard() {
		return packageStandard;
	}
	public void setPackageStandard(BigDecimal packageStandard) {
		this.packageStandard = packageStandard;
	}
	public String getPackageStandardCh() {
		return packageStandardCh;
	}
	public void setPackageStandardCh(String packageStandardCh) {
		this.packageStandardCh = packageStandardCh;
	}
	public String getErpBatch() {
		return erpBatch;
	}
	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}
	public Integer getMatId() {
		return matId;
	}
	public void setMatId(Integer matId) {
		this.matId = matId;
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
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	public BigDecimal getBeginQty() {
		return beginQty;
	}
	public void setBeginQty(BigDecimal beginQty) {
		this.beginQty = beginQty;
	}
	public BigDecimal getInputQty() {
		return inputQty;
	}
	public void setInputQty(BigDecimal inputQty) {
		this.inputQty = inputQty;
	}
	public BigDecimal getOutputQty() {
		return outputQty;
	}
	public void setOutputQty(BigDecimal outputQty) {
		this.outputQty = outputQty;
	}
	public BigDecimal getEndQty() {
		return endQty;
	}
	public void setEndQty(BigDecimal endQty) {
		this.endQty = endQty;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public Integer getFtyId() {
		return ftyId;
	}
	public void setFtyId(Integer ftyId) {
		this.ftyId = ftyId;
	}
	public boolean isPaging() {
		return paging;
	}
	public void setPaging(boolean paging) {
		this.paging = paging;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public boolean isSortAscend() {
		return sortAscend;
	}
	public void setSortAscend(boolean sortAscend) {
		this.sortAscend = sortAscend;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	@Override
	public void setSortAscend(String sortAscend) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<PageAggregate> getAggregateColumns() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setAggregateColumns(List<PageAggregate> aggregateColumns) {
		// TODO Auto-generated method stub
		
	}
	
	
}

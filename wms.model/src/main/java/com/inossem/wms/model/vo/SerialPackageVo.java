package com.inossem.wms.model.vo;

import java.math.BigDecimal;
import java.util.List;

import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;
import com.inossem.wms.model.serial.SerialPackage;

public class SerialPackageVo extends SerialPackage implements IPageCommon{
	
	private String statusName;
	private String packageTypeCode;
	private String packageTypeName;
	private String supplierCode;
	private String supplierName;
	private BigDecimal packageStandard;
	private String packageStandardCh;
	private String classificatName;
	
	public String getClassificatName() {
		return classificatName;
	}
	public void setClassificatName(String classificatName) {
		this.classificatName = classificatName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getPackageTypeCode() {
		return packageTypeCode;
	}
	public void setPackageTypeCode(String packageTypeCode) {
		this.packageTypeCode = packageTypeCode;
	}
	public String getPackageTypeName() {
		return packageTypeName;
	}
	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	private boolean paging = false;
    private int pageSize;
    private int pageIndex;
    private int totalCount = -1;
    private boolean sortAscend;
    private String sortColumn;
    
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
	}
	@Override
	public List<PageAggregate> getAggregateColumns() {
		return null;
	}
	@Override
	public void setAggregateColumns(List<PageAggregate> aggregateColumns) {
	}
    


}

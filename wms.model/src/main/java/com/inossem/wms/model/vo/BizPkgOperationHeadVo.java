package com.inossem.wms.model.vo;

import com.inossem.wms.model.biz.BizPkgOperationHead;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;
import com.inossem.wms.model.vo.BizPkgOperationItemVo;
import java.util.List;
import java.util.Map;

public class BizPkgOperationHeadVo extends BizPkgOperationHead implements IPageCommon {
	
    List<BizPkgOperationItemVo> itemList;
    
    private String ftyId;
    private String ftyName;
    private String createName;
    private String productLineName;
    private String installationName;
    private String receiptTypeName;
    private String pkgCreateTime;
    private String pkgModifyTime;
    private String statusName;
    private String classTypeName;
    List<DicProductLine> productLineList;
    List<Map<String, Object>>  classTypeList;
   
	private boolean paging = false;
    private int pageSize;
    private int pageIndex;
    private int totalCount = -1;
    private boolean sortAscend;
    private String sortColumn;
    
    public List<DicProductLine> getProductLineList() {
		return productLineList;
	}
	public void setProductLineList(List<DicProductLine> productLineList) {
		this.productLineList = productLineList;
	}
	public List<Map<String, Object>> getClassTypeList() {
		return classTypeList;
	}
	public void setClassTypeList(List<Map<String, Object>> classTypeList) {
		this.classTypeList = classTypeList;
	}
	public String getClassTypeName() {
		return classTypeName;
	}
	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}
    public String getFtyId() {
		return ftyId;
	}

	public void setFtyId(String ftyId) {
		this.ftyId = ftyId;
	}

    public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getPkgModifyTime() {
		return pkgModifyTime;
	}

	public void setPkgModifyTime(String pkgModifyTime) {
		this.pkgModifyTime = pkgModifyTime;
	}

	public String getPkgCreateTime() {
		return pkgCreateTime;
	}

	public void setPkgCreateTime(String pkgCreateTime) {
		this.pkgCreateTime = pkgCreateTime;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<BizPkgOperationItemVo> getItemList() {
        return this.itemList;
    }

    public void setItemList(List<BizPkgOperationItemVo> itemList) {
        this.itemList = itemList;
    }

    public String getReceiptTypeName() {
        return this.receiptTypeName;
    }

    public void setReceiptTypeName(String receiptTypeName) {
        this.receiptTypeName = receiptTypeName;
    }

    public String getInstallationName() {
        return this.installationName;
    }

    public void setInstallationName(String installationName) {
        this.installationName = installationName;
    }

    public String getFtyName() {
        return this.ftyName;
    }

    public void setFtyName(String ftyName) {
        this.ftyName = ftyName;
    }

    public String getProductLineName() {
        return this.productLineName;
    }

    public void setProductLineName(String productLineName) {
        this.productLineName = productLineName;
    }

    public boolean isPaging() {
        return this.paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isSortAscend() {
        return this.sortAscend;
    }

    public void setSortAscend(boolean sortAscend) {
        this.sortAscend = sortAscend;
    }

    public String getSortColumn() {
        return this.sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public void setSortAscend(String sortAscend) {
    }

    public List<PageAggregate> getAggregateColumns() {
        return null;
    }

    public void setAggregateColumns(List<PageAggregate> aggregateColumns) {
    }
}

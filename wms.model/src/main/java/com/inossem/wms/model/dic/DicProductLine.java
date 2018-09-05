package com.inossem.wms.model.dic;

import java.util.List;

import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class DicProductLine  implements IPageCommon{
    private Integer productLineId;
    private String productLineName;
    private Byte isDelete;
    private Byte isUsed;
    
    
    
    public Byte getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Byte isUsed) {
		this.isUsed = isUsed;
	}

	private List<DicInstallation> installations;
    
    public Integer getProductLineId() {
        return this.productLineId;
    }

    public void setProductLineId(Integer productLineId) {
        this.productLineId = productLineId;
    }

    public String getProductLineName() {
        return this.productLineName;
    }

    public void setProductLineName(String productLineName) {
        this.productLineName = productLineName == null?null:productLineName.trim();
    }

    public Byte getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

	public List<DicInstallation> getInstallations() {
		return installations;
	}

	public void setInstallations(List<DicInstallation> installations) {
		this.installations = installations;
	}

	@Override
	public boolean isSortAscend() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSortAscend(boolean sortAscend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSortAscend(String sortAscend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSortColumn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSortColumn(String sortColumn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPaging() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPaging(boolean paging) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPageIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPageIndex(int pageIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPageSize(int pageSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTotalCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTotalCount(int totalCount) {
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

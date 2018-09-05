package com.inossem.wms.model.dic;

import java.util.Date;
import java.util.List;

import com.inossem.wms.model.page.IPageCommon;
import com.inossem.wms.model.page.PageAggregate;

public class DicClassType implements IPageCommon{
    private Integer classTypeId;
    private String classTypeName;
    private Date startTime;
    private Date endTime;
    private Byte isDelete;

    public DicClassType() {
    }

    public Integer getClassTypeId() {
        return this.classTypeId;
    }

    public void setClassTypeId(Integer classTypeId) {
        this.classTypeId = classTypeId;
    }

    public String getClassTypeName() {
        return this.classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName == null?null:classTypeName.trim();
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Byte getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
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

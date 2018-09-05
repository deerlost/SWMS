package com.inossem.wms.model.dic;

import java.util.Date;
import java.util.List;

public class DicCorp {
	
	private List<DicWarehouse> warehouseList;
	
   

	public List<DicWarehouse> getWarehouseList() {
		return warehouseList;
	}

	public void setWarehouseList(List<DicWarehouse> warehouseList) {
		this.warehouseList = warehouseList;
	}

	private Integer corpId;

    private String corpCode;

    private String corpName;

    private Integer cityId;

    private Integer boardId;

    private Byte isDelete;

    private Date createTime;

    private Date modifyTime;

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode == null ? null : corpCode.trim();
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName == null ? null : corpName.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
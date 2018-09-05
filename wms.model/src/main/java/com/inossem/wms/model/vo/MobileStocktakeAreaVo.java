package com.inossem.wms.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端盘点存储区列表用
 * @author wang.ligang
 *
 */
public class MobileStocktakeAreaVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	private String prefix;            // 区域号：存储区， 显示内容，是来自盘点明细新增字段
	private String areaCode;       // 区域号值： 存储区
	private String current;               // 已经盘点的种类: 盘点数>0  有一个就加1
	private String total;                 // 区域下所有物料种类（由仓位+批次+物料编号区分唯一标识）
	private String areaId;				// 
	
	
	private List<MobileStocktakeAreaPositionVo> positionList;// 仓位列表
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public List<MobileStocktakeAreaPositionVo> getPositionList() {
		return positionList;
	}
	public void setPositionList(List<MobileStocktakeAreaPositionVo> positionList) {
		this.positionList = positionList;
	}
	
	
	
	
}

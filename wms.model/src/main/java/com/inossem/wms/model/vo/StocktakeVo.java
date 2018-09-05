package com.inossem.wms.model.vo;

import java.util.List;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.page.IPageResultCommon;

public class StocktakeVo implements IPageResultCommon{
	
	//private StockTakeHeadVo head; // 库存盘点抬头信息
	private StocktakeItemVo item; // 库存盘点明细表信息
	private List<StocktakeItemVo> items; // 库存盘点明细列表
	
	private CurrentUser createUser; 			// 盘点单创建人
	private CurrentUser checkUser;  			// 盘点单盘点人
	
	private String condition; 					// 模糊查询的字符串
	
	private String status;						// 状态值，中间’-’分隔
	private String[] statusArray; 				// 状态值的数组
	
	private String ymbh; 						// 用于调用统一接口返回不同数据的比对值
	private Boolean submit = false;             // 暂存传false,提交传true          
	
	private int pageSize;
	private int pageIndex;
	private int totalCount = -1;
	
	@Override
	public int getTotalCount() {
		// TODO Auto-generated method stub
		return totalCount;
	}
	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
//	public StocktakeHeadVo getHead() {
//		return head;
//	}
//	public void setHead(StocktakeHeadVo head) {
//		this.head = head;
//	}
	public StocktakeItemVo getItem() {
		return item;
	}
	public void setItem(StocktakeItemVo item) {
		this.item = item;
	}
	public List<StocktakeItemVo> getItems() {
		return items;
	}
	public void setItems(List<StocktakeItemVo> items) {
		this.items = items;
	}
	public CurrentUser getCreateUser() {
		return createUser;
	}
	public void setCreateUser(CurrentUser createUser) {
		this.createUser = createUser;
	}
	public CurrentUser getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(CurrentUser checkUser) {
		this.checkUser = checkUser;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		if (status != null) {
			this.statusArray = status.split("-");
		}
		this.status = status;
	}
	public String[] getStatusArray() {
		return statusArray;
	}
	public void setStatusArray(String[] statusArray) {
		this.statusArray = statusArray;
	}
	public String getYmbh() {
		return ymbh;
	}
	public void setYmbh(String ymbh) {
		this.ymbh = ymbh;
	}
	public Boolean getSubmit() {
		return submit;
	}
	public void setSubmit(Boolean submit) {
		this.submit = submit;
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
	
}

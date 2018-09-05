package com.inossem.wms.model.vo;

import com.inossem.wms.model.biz.BizAllocateCargoHead;
import com.inossem.wms.model.page.IPageResultCommon;

public class BizAllocateCargoHeadVo extends BizAllocateCargoHead implements IPageResultCommon{
	private int totalCount = -1;
	private String create_user;
	
	private String create_time;
	
	private String status_name;
	
	
	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	@Override
	public int getTotalCount() {		
		return 0;
	}

	@Override
	public void setTotalCount(int totalCount) {		
		this.totalCount=totalCount;
	}

}

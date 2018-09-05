package com.inossem.wms.model;

import com.inossem.wms.model.page.PageCommon;

/**
 * 基本实体 分页 排序
 * 
 * @author maoy
 *
 */
public class BaseEntity extends PageCommon {

	/**
	 * sql查询语句
	 */
	private String sql;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}

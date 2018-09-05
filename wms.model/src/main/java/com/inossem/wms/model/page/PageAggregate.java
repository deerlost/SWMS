package com.inossem.wms.model.page;

//聚合函数类,如果不只需要统计总数的情况使用
public class PageAggregate {

	// 表达式
	private String expression;
	// 列别名
	private String alias;

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}

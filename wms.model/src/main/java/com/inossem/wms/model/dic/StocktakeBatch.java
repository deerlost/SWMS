package com.inossem.wms.model.dic;

public class StocktakeBatch {
	private String id;     // 特性id
	private String type;   // 特性类型
	private String value;  // 特性值
	private String text;   // 特性中文名
	
	public StocktakeBatch() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

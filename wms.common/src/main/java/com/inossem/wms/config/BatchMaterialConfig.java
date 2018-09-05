package com.inossem.wms.config;

public class BatchMaterialConfig {
	public int index;
	public boolean edit;
	public String columnName;
	public String batchSpecCode;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getBatchSpecCode() {
		return batchSpecCode;
	}

	public void setBatchSpecCode(String batchSpecCode) {
		this.batchSpecCode = batchSpecCode;
	}

}

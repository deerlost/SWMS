package com.inossem.wms.model.vo;

import com.inossem.wms.model.dic.DicMaterial;

public class DicMaterialVo extends DicMaterial {
	private String name_en;
	private String name_zh;
	private String unit_code;

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public String getName_zh() {
		return name_zh;
	}

	public void setName_zh(String name_zh) {
		this.name_zh = name_zh;
	}

	public String getUnit_code() {
		return unit_code;
	}

	public void setUnit_code(String unit_code) {
		this.unit_code = unit_code;
	}

}

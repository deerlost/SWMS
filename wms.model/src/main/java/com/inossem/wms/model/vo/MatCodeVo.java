package com.inossem.wms.model.vo;

/**
 * 物料编码截取
 * 
 * @author 刘宇 2018.02.06
 *
 */
public class MatCodeVo {
	private String begin; // 物料编码区间开始的编码

	private String end;// 物料编码区间结束的编码

	private String[] strings;// 单个或多个物料编码数组

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String[] getStrings() {
		return strings;
	}

	public void setStrings(String[] strings) {
		this.strings = strings;
	}

}

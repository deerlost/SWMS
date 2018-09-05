package com.inossem.wms.model.page;

public class PageWord {
	private int begin;
	private int length;
	private String text;

	PageWord(int begin, int length, String text) {
		this.begin = begin;
		this.length = length;
		this.text = text;
	}

	public int getBegin() {
		return begin;
	}

	public int getLength() {
		return length;
	}

	public String getText() {
		return text;
	}

}

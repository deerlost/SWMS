package com.inossem.wms.model.auth;

public class MenuADK implements Comparable<MenuADK>{
	private String id;
	private String name;
	
	private int index;
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int compareTo(MenuADK o) {
		int i = this.getIndex()-o.getIndex();
		
		return i;
	}
	
	
	
}

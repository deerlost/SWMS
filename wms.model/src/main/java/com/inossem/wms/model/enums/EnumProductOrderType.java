package com.inossem.wms.model.enums;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产订单类型
 * @author sw
 *
 */
public enum EnumProductOrderType {
	
	ZP01("标准生产订单","ZP01"),
	
	ZP02("返工生产订单","ZP02"),
	
	ZP03("来料加工生产订单","ZP03"),
	
	ZP04("在制品订单","ZP04"),
	
	ZP05("公用工程生产订单","ZP05"),
	
	ZPW1("化纤类成品生产订单","ZPW1");
	
	private EnumProductOrderType(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/** 描述 */
	private String name;
	
	/** 枚举值 */
	private String value;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	private static Map<String, String> map;
	
	private static List<Map<String, String>> list;
	
	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumProductOrderType[] ary = EnumProductOrderType.values();
			List<Map<String, String>> listTmp = new ArrayList<Map<String, String>>();
			for (int i = 0; i < ary.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", String.valueOf(ary[i].getValue()));
				map.put("name", ary[i].getName());
				listTmp.add(map);
			}
			list = listTmp;
		}
		return list;
	}
	
	public static Map<String, String> toMap() {
		if (map == null) {
			EnumProductOrderType[] ary = EnumProductOrderType.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}
	
	public static String getNameByValue(String value) {
		return toMap().get(value);
	}
	
}

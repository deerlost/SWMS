package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumTurnOverType {

	 ALL("全部", 3),
	 TURN_OVER_TIMES("周转次数", 2),
	 TURN_OVER_DAY("周转天数", 1);
	/** 描述 */
	private String name;
	/** 枚举值 */
	private int value;

	private EnumTurnOverType(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private static List<Map<String, String>> list;
	private static Map<Integer, String> map;
	private static List<Map<String, Object>> boardList;
	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumTurnOverType[] ary = EnumTurnOverType.values();
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
	
	public static List<Map<String, Object>> toBoardList() {
		if (boardList == null) {
			EnumTurnOverType[] ary = EnumTurnOverType.values();
			List<Map<String, Object>> listTmp = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < ary.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("boardId", ary[i].getValue());
				map.put("boardName", ary[i].getName());
				listTmp.add(map);
			}
			boardList = listTmp;
		}
		return boardList;
	}

	public static Map<Integer, String> toMap() {
		if (map == null) {
			EnumTurnOverType[] ary = EnumTurnOverType.values();
			Map<Integer, String> enumMap = new HashMap<Integer, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}

	public static String getNameByValue(int value) {
		return toMap().get(value);
	}



}

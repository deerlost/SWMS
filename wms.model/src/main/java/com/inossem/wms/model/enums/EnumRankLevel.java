package com.inossem.wms.model.enums;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mes产品等级
 * @author sw
 *
 */
public enum EnumRankLevel {
	
	//SEMI_FINISHED("半成品","-2",""),
	
	SUPERIOR("优等品","1","9"),
	
	FIRST_GRADE("一等品","2","1"),
	
	QUALIFIED("合格品","5","0"),
	
	NO_RANK("（无等级）","12","W"),
	
	UNSPECIFIED("未指定","-1","X"),
	
	FOREIGN("等外品","10","A"),
	
	WASTE("废品","13","F"),
	
	//DISABLE_1("禁用（不确定等级）","14","X"),

	//DISABLE_2("禁用（在制品专用等级）","15","P"),
	
	INFERIOR("次品","16","C");
	
	
	private EnumRankLevel(String name,String rank,String value) {
		this.name = name;
		this.value = value.substring(0, 1);
		this.rank = rank;
	}
	
	/** 描述 */
	private String name;
	/** 等级 */
	private String rank;
	/** 枚举值 */
	private String value;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	private static Map<String, String> nameMap;
	
	private static Map<String, String> rankMap;
	
	private static List<Map<String, String>> list;
	
	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumRankLevel[] ary = EnumRankLevel.values();
			List<Map<String, String>> listTmp = new ArrayList<Map<String, String>>();
			for (int i = 0; i < ary.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", String.valueOf(ary[i].getValue()));
				map.put("name", ary[i].getName());
				map.put("rank", ary[i].getRank());
				listTmp.add(map);
			}
			list = listTmp;
		}
		return list;
	}
	
	public static Map<String, String> toRankMap() {
		if (rankMap == null) {
			EnumRankLevel[] ary = EnumRankLevel.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getRank());
			}
			rankMap = enumMap;
		}
		return rankMap;
	}
	
	public static Map<String, String> toNameMap() {
		if (nameMap == null) {
			EnumRankLevel[] ary = EnumRankLevel.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			nameMap = enumMap;
		}
		return nameMap;
	}
	
	public static String getNameByValue(String value) {
		return toNameMap().get(value.substring(0, 1));
	}
	
	public static String getRankByValue(String value) {
		return toRankMap().get( value.substring(0, 1));
	}
}

package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumVolumeStatisticsType {

	INPUT("入库","0","0"), 
	OUTPUT("出库","0","0"), 
	TASK_UP("上架","0","0"), 
	TASK_DOWN("下架","0","0"), 
	TRANSPORT_OUT("转储出","0","0"), 
	TRANSPORT_IN("转储入","0","0"), 
	TRANTAK_OUT("转运出","0","0"), 
	TRANTAK_IN("转运入","0","0"), 	
	TAKE("盘点","0","0"), 
	ALL("总计","1","0"), 
	PDA("PDA操作笔数","1","0"),
	PDA_PERCENT("PDA应用率","1","1");
	

	/** 描述 */
	private String name;
	/** 枚举值 */
	private String type;
	
	private String dependence;

	private EnumVolumeStatisticsType(String name, String type,String dependence) {
		this.name = name;
		this.type = type;
		this.dependence = dependence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDependence() {
		return dependence;
	}

	public void setDependence(String dependence) {
		this.dependence = dependence;
	}



	
	private static List<Map<String, Object>> typeList;
	private static Map<String, String> map;
	
	public static List<Map<String, Object>> toTypeList() {
		if (typeList == null) {
			EnumVolumeStatisticsType[] ary = EnumVolumeStatisticsType.values();
			List<Map<String, Object>> listTmp = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < ary.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type", ary[i].getType());
				map.put("name", ary[i].getName());
				map.put("dependence", ary[i].getDependence());
				listTmp.add(map);
			}
			typeList = listTmp;
		}
		return typeList;
	}

	public static Map<String, String> toMap() {
		if (map == null) {
			EnumVolumeStatisticsType[] ary = EnumVolumeStatisticsType.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getType(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}

	public static String getNameByType(String value) {
		return toMap().get(value);
	}

}

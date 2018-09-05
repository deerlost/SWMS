package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点仓位状态：1-非空仓位，2-空仓位，3-全部
 * @author wang.ligang
 *
 */
public enum EnumStocktakePositionStatus {
	/** 非空仓位 **/
	NOEMPTY("非空仓位", "1"),
	
	/** 空仓位 **/
	EMPTY("空仓位", "2"),
	
	/** 全部 **/
	ALL("全部", "3");

	/** 描述 */
	private String desc;
	/** 枚举值 */
	private String value;

	private EnumStocktakePositionStatus(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static EnumStocktakePositionStatus getEnum(String value){
		EnumStocktakePositionStatus resultEnum = null;
		EnumStocktakePositionStatus[] enumAry = EnumStocktakePositionStatus.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue().equals(value)){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumStocktakePositionStatus[] ary = EnumStocktakePositionStatus.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put("value", ary[num].getValue());
			map.put("desc", ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		EnumStocktakePositionStatus[] ary = EnumStocktakePositionStatus.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value",ary[i].getValue());
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static String getDescByValue(String value) {
		String ret = "";
		List<Map<String,String>> list = EnumStocktakePositionStatus.toList();
		for (Map<String,String> temp : list) {
			String key = temp.get("value");
			if (value != null && value.equals(key)) {
				ret = temp.get("desc");
				break;
			}
		}
		return ret;
	}
}

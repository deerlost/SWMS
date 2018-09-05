package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点行项目状态
 * @author wang.ligang
 *
 */
public enum EnumStocktakeItemStatus {
	/** 草稿1,添加数据之后的默认状态，这个状态不在UI显示 **/
	DRAFT("草稿", "1"),
	
	/** 未完成 2 **/
	INCOMPLETE("未完成", "2"),
	
	/** 已完成3 **/
	COMPLETED("已完成", "3"),
	
	/** 已提交 4 **/
	POSTED("已提交", "4"),
	
	/** 重盘 6 **/
	RESTOCKTAK("重盘", "6");

	/** 描述 */
	private String desc;
	/** 枚举值 */
	private String value;

	private EnumStocktakeItemStatus(String desc, String value) {
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
	
	public static EnumStocktakeItemStatus getEnum(String value){
		EnumStocktakeItemStatus resultEnum = null;
		EnumStocktakeItemStatus[] enumAry = EnumStocktakeItemStatus.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue().equals(value)){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumStocktakeItemStatus[] ary = EnumStocktakeItemStatus.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			if (ary[num].getValue().equals(EnumStocktakeItemStatus.DRAFT.getValue())) {
				continue;
			}
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
		EnumStocktakeItemStatus[] ary = EnumStocktakeItemStatus.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			if (ary[i].getValue().equals(EnumStocktakeItemStatus.DRAFT.getValue())) {
				continue;
			}
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
		List<Map<String,String>> list = EnumStocktakeItemStatus.toList();
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

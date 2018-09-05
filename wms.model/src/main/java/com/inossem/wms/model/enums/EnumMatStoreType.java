package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumMatStoreType {
	
	USEING("启用包装物", (byte) 1),
	UN_USEING("不启用包装物", (byte) 2);
	
	
	/** 描述 */
	private String desc;
	/** 枚举值 */
	private byte value;

	private EnumMatStoreType(String desc, byte value) {
		this.desc = desc;
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static EnumMatStoreType getEnum(byte value){
		EnumMatStoreType resultEnum = null;
		EnumMatStoreType[] enumAry = EnumMatStoreType.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue() == value){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumMatStoreType[] ary = EnumMatStoreType.values();
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
		EnumMatStoreType[] ary = EnumMatStoreType.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("value",ary[i].getValue());
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static String getDescByValue(byte value) {
		String ret = "";
		List<Map<String,Object>> list = EnumMatStoreType.toList();
		for (Map<String,Object> temp : list) {
			byte key = (Byte)temp.get("value");
			if (value == key) {
				ret = (String)temp.get("desc");
				break;
			}
		}
		return ret;
	}




}

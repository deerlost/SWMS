package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumAllocateStatus {
	
	DRAFT("草稿", (byte) 0),
	POSTED("已提交", (byte) 10),
	WAIT_SEND("待配送", (byte) 15),
	WAIT_OUTPUT("待出库", (byte) 17),
	SENDING("配送中", (byte) 20),
	WAIT_INPUT("待入库", (byte) 25),
	COMPLETED("已完成", (byte) 30);
	
	/** 描述 */
	private String desc;
	/** 枚举值 */
	private byte value;

	private EnumAllocateStatus(String desc, byte value) {
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
	
	public static EnumAllocateStatus getEnum(byte value){
		EnumAllocateStatus resultEnum = null;
		EnumAllocateStatus[] enumAry = EnumAllocateStatus.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue() == value){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumAllocateStatus[] ary = EnumAllocateStatus.values();
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
		EnumAllocateStatus[] ary = EnumAllocateStatus.values();
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
		List<Map<String,Object>> list = EnumAllocateStatus.toList();
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

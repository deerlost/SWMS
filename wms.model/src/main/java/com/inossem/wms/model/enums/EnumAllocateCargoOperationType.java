package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumAllocateCargoOperationType {


    // 生产
	NORMAL("正常配货", (byte) 1),
    // 销售
    NO_MENTION("已售未提配货", (byte) 2);

    /** 描述 */
    private String name;
    /** 枚举值 */
    private Byte value;

    private EnumAllocateCargoOperationType(String name, Byte value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    private static List<Map<String, String>> list;
    private static Map<Byte, String> map;

    public static List<Map<String, String>> toList() {
        if (list == null) {
            EnumAllocateCargoOperationType[] ary = EnumAllocateCargoOperationType.values();
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

    public static Map<Byte, String> toMap() {
        if (map == null) {
            EnumAllocateCargoOperationType[] ary = EnumAllocateCargoOperationType.values();
            Map<Byte, String> enumMap = new HashMap<Byte, String>();
            for (int num = 0; num < ary.length; num++) {
                enumMap.put(ary[num].getValue(), ary[num].getName());
            }
            map = enumMap;
        }
        return map;
    }

    public static String getNameByValue(Byte value) {
        return toMap().get(value);
    }



}

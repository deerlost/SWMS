package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumIsUrgent {
    // 非紧急记账
    NO_URGENT("非紧急作业", (byte) 0),
    // 紧急记账
    URGENT("紧急作业", (byte) 1);

    /** 描述 */
    private String name;
    /** 枚举值 */
    private Byte value;

    private EnumIsUrgent(String name, Byte value) {
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
            EnumIsUrgent[] ary = EnumIsUrgent.values();
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
            EnumIsUrgent[] ary = EnumIsUrgent.values();
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

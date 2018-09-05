package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * MES失败判断 
 * @author sw
 *
 */
public enum EnumMesErrorInfo {
	
	TYPE_1("目标物料","请联系MES关键用户维护物料和仓库关系"),
	
	TYPE_2("目标库位","请联系MES关键用户维护物料和仓库关系"),
	
	TYPE_3("不匹配","请联系MES关键用户维护物料和仓库关系"),
	
	TYPE_4("来源节点","请联系MES关键用户维护物料和仓库关系"),
	
	TYPE_5("目标节点","请联系MES关键用户维护物料和仓库关系"),
	
	TYPE_6("负库存","请检查MES库存是否存在,并且重新同步MES"),
	
	TYPE_7("源物料","请联系MES关键用户维护物料和仓库关系");
	
	 /** 错误信息 */
    private String name;
	
	 /** 添加描述 */
    private String desc;

    private EnumMesErrorInfo(String name,String desc) {
        this.name = name;
        this.desc = desc;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private static List<Map<String,Object>> list;
    
    public static List<Map<String,Object>> toList() {
		if (list == null) {
			EnumMesErrorInfo[] ary = EnumMesErrorInfo.values();
			List<Map<String,Object>> listTmp = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < ary.length; i++) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", ary[i].getName());
				map.put("desc", ary[i].getDesc());
				listTmp.add(map);
			}
			list = listTmp;
		}
		return list;
	}
    
	public static String getDescByName(String name) {
		String ret = "";
		List<Map<String,Object>> list = EnumMesErrorInfo.toList();
		for (Map<String,Object> temp : list) {
			String key = (String)temp.get("name");
			if (name == key) {
				ret = (String)temp.get("desc");
				break;
			}
		}
		return ret;
	}
    
    public static String check(String name) {
    	String result = name;
    	for(Map<String,Object> map : toList()) {
		    Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
	    	while(iter.hasNext()){
	    		Entry<String, Object> entry = iter.next();
			    String key = (String) entry.getValue();
			    if(name.contains(key)) {
	    			result += " , " + getDescByName(key);
	    			return result;
	    		}
		    }
    	}
    	result += " , 因MES问题请稍后重新同步MES"; 
    	return result;
    }
}

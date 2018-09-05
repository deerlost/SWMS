package com.inossem.wms.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class UtilJSON {

	public static Date getDateTimeFromJSON(String key, JSONObject obj) {
		Date ret = null;
		try {
			if (obj.containsKey(key)) {
				Object value = obj.get(key);
				ret = UtilString.getDateTimeForString(value.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Date getDateFromJSON(String key, JSONObject obj) {
		Date ret = null;
		try {
			if (obj.containsKey(key)) {
				Object value = obj.get(key);
				ret = UtilString.getDateForString(value.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static long getLongFromJSON(String key, JSONObject obj) {
		long ret = 0;
		try {
			if (obj.containsKey(key)) {
				Object value = obj.get(key);
				if (value instanceof String) {
					ret = Long.parseLong((String) value);
				} else if (value instanceof Long) {
					ret = (long) value;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static int getIntFromJSON(String key, JSONObject obj) {
		int ret = 0;
		try {
			if (obj.containsKey(key)) {
				Object value = obj.get(key);
				if (value instanceof String) {
					ret = Integer.parseInt((String) value);
				} else if (value instanceof Integer) {
					ret = (int) value;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String getStringOrNullFromJSON(String key, JSONObject obj) {
		String ret = null;
		try {
			if (obj.containsKey(key)) {
				Object value = obj.getString(key);
				if (value instanceof String) {
					ret = (String) value;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String getStringOrEmptyFromJSON(String key, JSONObject obj) {
		String ret = "";
		try {
			if (obj.containsKey(key)) {
				Object value = obj.getString(key);
				if (value instanceof String) {
					ret = (String) value;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.trim();
	}

	public static BigDecimal getBigDecimalFromJSON(String key, JSONObject obj) {
		BigDecimal ret = null;
		try {
			if (obj.containsKey(key)) {
				Object value = obj.getString(key);
				if (value instanceof String) {
					ret = new BigDecimal((String) value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static byte getByteFromJSON(String key, JSONObject obj) {
		byte ret = 0;
		try {
			if (obj.containsKey(key)) {
				Object value = obj.get(key);
				if (value instanceof String) {
					ret = Byte.parseByte((String) value);
				} else if (value instanceof Byte) {
					ret = (Byte) value;
				} else {
					ret = Byte.parseByte(value.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * @Description: 将 Json 串 解析成 Map或者javabean
	 * @param jsonStr
	 *            json串，必须传递
	 * @param clazz
	 *            返回的对象类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonStrToObject(String jsonStr, Class<T> clazz) {
		JSONObject json = JSONObject.fromObject(jsonStr);
		Object obj = JSONObject.toBean(json, clazz);
		return (T) obj;
	}

	/**
	 * 将 Array,list,set 解析成 Json 串
	 * 
	 * @return Json 串
	 */
	public static String arrayToJsonStr(Object objs) {
		JSONArray json = JSONArray.fromObject(objs);
		return json.toString();
	}

	/***
	 * 将javabean对象和map对象 解析成 Json 串
	 * 
	 * @param obj
	 * @return
	 */
	public static String objectToJsonStr(Object obj) {
		JSONObject json = JSONObject.fromObject(obj);
		return json.toString();

	}

	/***
	 * 将javabean对象或者map对象 解析成 Json 串,使用JsonConfig 过滤属性
	 * 
	 * @param obj
	 * @param config
	 * @return
	 */
	public static String objectToJsonStr(Object obj, JsonConfig config) {
		// JsonConfig config = new JsonConfig();
		// config.setExcludes(new String[] { "name" });
		JSONObject json = JSONObject.fromObject(obj, config);
		return json.toString();

	}

	/**
	 * 将 Json 串 解析成 Array,list,set
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> jsonStrToArray(String jsonStr) {
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		Object array = JSONArray.toArray(jsonArray);
		return (Collection<T>) array;
	}

	/**
	 * 将 Json 串 解析成 Array
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] jsonStrToArray(String jsonStr, Class<T> clazz) {
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		return (T[]) JSONArray.toArray(jsonArray, clazz);
	}

	/**
	 * 将 Json 串 解析成 Collection
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> jsonStrToCollection(String jsonStr, Class<T> clazz) {
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		Collection<T> array = JSONArray.toCollection(jsonArray, clazz);
		return array;
	}

	/**
	 * 将 Json 串 解析成 list
	 * 
	 * @return
	 */
	public static <T> List<T> jsonStrToList(String jsonStr, Class<T> clazz) {
		return (List<T>) jsonStrToCollection(jsonStr, clazz);
	}

	/**
	 * JSONArray排序
	 * 
	 * @param array
	 * @return
	 */
	@SuppressWarnings("all")
	public static JSONArray sortJsonArray(JSONArray array) {
		List list = new ArrayList();
		int size = array.size();
		for (int i = 0; i < size; i++) {
			Object obj = array.get(i);
			if (obj instanceof JSONObject) {
				list.add(sortJsonObject(JSONObject.fromObject(obj)));
			} else if (obj instanceof JSONArray) {
				list.add(sortJsonArray(JSONArray.fromObject(obj)));
			} else {
				list.add(obj);
			}
		}

		list.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		return JSONArray.fromObject(list);
	}

	/**
	 * JSONObject排序
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("all")
	public static JSONObject sortJsonObject(JSONObject obj) {
		Map map = new TreeMap();
		Iterator<String> it = obj.keys();
		while (it.hasNext()) {
			String key = it.next();
			Object value = obj.get(key);
			if (value instanceof JSONObject) {
				map.put(key, sortJsonObject(JSONObject.fromObject(value)));
			} else if (value instanceof JSONArray) {
				map.put(key, sortJsonArray(JSONArray.fromObject(value)));
			} else {
				map.put(key, value);
			}
		}
		return JSONObject.fromObject(map);
	}
	
	public static boolean getBoolOrFalseFromJSON(String key, JSONObject obj) {
		Boolean ret = false;
		try {
			if (obj.containsKey(key)) {
				Object value = obj.getBoolean(key);
				if (value instanceof Boolean) {
					ret = (Boolean) value;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void main(String[] args) {
//		JSONObject object = new JSONObject();
//		object.put("submit", true);
//		System.out.println(getBoolOrFalseFromJSON("submit", object));
	}
}

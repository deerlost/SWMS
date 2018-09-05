package com.inossem.wms.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 全局JSON转换类
 * @author wang.ligang
 *
 */
public class UtilJSONConvert {
	/**
	 * 将json的全部key转换为下划线的snake case key 多层结构时递归调用
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	private final static void convert(Object json) {
		if (json instanceof JSONArray) {
			JSONArray arr = (JSONArray) json;
			for (Object obj : arr) {
				convert(obj);
			}
		} else if (json instanceof JSONObject) {
			JSONObject jo = (JSONObject) json;
			Set<String> keys = jo.keySet();
			String[] array = keys.toArray(new String[keys.size()]);
			for (String key : array) {
				Object value = jo.get(key);

				String snake_key = underscoreName(key);
				jo.remove(key);
				
				convert(value);
				jo.put(snake_key, value);
			}
		} else if (json instanceof JsonArray) {
			JsonArray arr = (JsonArray) json;
			for (JsonElement obj : arr) {
				if(obj.isJsonArray()){
					convert(obj.getAsJsonArray());
				}else if(obj.isJsonObject()){
					convert(obj.getAsJsonObject());
				}
			}
		} else if (json instanceof JsonObject) {
			JsonObject jo = (JsonObject) json;
			Set<Entry<String, JsonElement>> keys = jo.entrySet();
			Set<Entry<String, JsonElement>> cloneKeys = new HashSet<Map.Entry<String,JsonElement>>();
			cloneKeys.addAll(keys);
			for (Entry<String, JsonElement> key : cloneKeys) {
				JsonElement value = key.getValue();

				String snake_key = underscoreName(key.getKey());
				jo.remove(key.getKey());
				if(value.isJsonArray()){
					convert(value.getAsJsonArray());
				}else if(value.isJsonObject()){
					convert(value.getAsJsonObject());
				}
				jo.add(snake_key, value);
			}
		}
		
	}

	/**
	 * 将一个驼峰型字符串转换为snake型字符串
	 * 
	 * @param name
	 * @return
	 */
	private static String underscoreName(String name) {
		if (StringUtils.isEmpty(name)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(name.substring(0, 1).toLowerCase());
		for (int i = 1; i < name.length(); ++i) {
			String s = name.substring(i, i + 1);
			String slc = s.toLowerCase();
			if (!(s.equals(slc))) {
				result.append("_").append(slc);
			} else {
				result.append(s);
			}
		}
		return result.toString();
	}

	/**
	 * Web端将json的key转换为下划线的snake case key
	 * 
	 * @param json
	 * @return
	 */
	public final static Object convert(String json) {
		//Object obj = JSONObject.fromObject(json);
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(json);
		Object obj = el.getAsJsonObject();
		convert(obj);
		return obj;
	}
	
	/**
	 * APP端将json的key转换为下划线的snake case key
	 * 
	 * @param json
	 * @return
	 */
	public final static Object convertForApp(String json) {
		// 设置json格式为字符串形式
		
	//	Object obj = JSONObject.fromObject(json);
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(json);
		Object obj = el.getAsJsonObject();
		
		// 将值为非String类型转换为String类型后变为字符串
		String asValueToString = object2json(obj);
	//	Object objAsValueToString = JSONObject.fromObject(asValueToString);
		JsonElement elToString = parser.parse(asValueToString);
		Object objAsValueToString = elToString.getAsJsonObject();
		convert(objAsValueToString);
		return objAsValueToString;
		
	}
    
    /**
     * 将json的key转换为驼峰key
     * @param json
     */
	public static String humpName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        boolean underscore = false;
        for (int i = 1; i < name.length(); ++i) {
            String s = name.substring(i, i + 1);
            if ("_".equals(s)) {
                underscore = true;
                continue;
            } else {
                if (underscore) s = s.toUpperCase();
                underscore = false;
            }
            result.append(s);
        }
        return result.toString();
    }
    
    @SuppressWarnings("unchecked")
	public final static void convertToHump(Object json) {
        if (json instanceof JSONArray) {
            JSONArray arr = (JSONArray) json;
            for (Object obj : arr) {
            	convertToHump(obj);
            }
        } else if (json instanceof JSONObject) {
        	JSONObject jo = (JSONObject) json;
			Set<String> keys = jo.keySet();
            String[] array = keys.toArray(new String[keys.size()]);
            for (String key : array) {
        	  Object value = jo.get(key);
        	  
        	  String snake_key = humpName(key);
        	  jo.remove(key);
        	  
        	  convertToHump(value);
        	  jo.put(snake_key, value);
          }
        }
    }
    
    /**
	 * 将 Json 转 实体
	 * @return
	 */
	public static <T> T fromJsonToHump(JSONObject json, Class<T> classOfT){
		convertToHump(json);
		T t = new Gson().fromJson(UtilJSON.objectToJsonStr(json),classOfT);
		return t;
	}
	
	/**
     * 入口方法（将ObjectJson值转换为字符串）
     * 
     * @param obj
     * @return
     */
    public static String object2json(Object obj) {
        StringBuilder json = new StringBuilder();  
        if (obj == null) {  
            json.append("\"\"");  
        } else if (obj instanceof String || obj instanceof Integer || obj instanceof Float  
             || obj instanceof Boolean || obj instanceof Short || obj instanceof Double  
             || obj instanceof Long || obj instanceof BigDecimal || obj instanceof BigInteger  
             || obj instanceof Byte) {
        	if(obj instanceof BigDecimal){
        		json.append("\"").append(string2json(((BigDecimal) obj).toPlainString())).append("\"");  
        	}else{
        		json.append("\"").append(string2json(obj.toString())).append("\"");  
        	}
            
        } else if (obj instanceof Object[]) {  
            json.append(array2json((Object[]) obj));  
        } else if (obj instanceof List) {  
            json.append(list2json((List<?>) obj));  
        } else if (obj instanceof Map) {  
            json.append(map2json((Map<?, ?>) obj));  
        } else if (obj instanceof Set) {  
            json.append(set2json((Set<?>) obj));  
        } else if (obj instanceof JsonObject) {  
            json.append(Gson2json((JsonObject)obj));  
        } else if (obj instanceof JsonArray) {  
            json.append(GsonAry2json((JsonArray)obj));  
        } else {  
            json.append(bean2json(obj));  
        }  
        return json.toString();
    }
    
    private static String string2json(String s) {  
        if (s == null)  
            return "";  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < s.length(); i++) {  
            char ch = s.charAt(i);  
            switch (ch) {  
                case '"':  
                    sb.append("\\\"");
                    break;
                case '\\':
                     sb.append("\\\\");
                     break;
                 case '\b':
                     sb.append("\\b");
                     break;
                 case '\f':
                     sb.append("\\f");
                     break;
                 case '\n':
                     sb.append("\\n");
                     break;
                 case '\r':
                     sb.append("\\r");
                     break;
                 case '\t':
                     sb.append("\\t");
                     break;
                 case '/':
                     sb.append("\\/");
                     break;
                 default:
                     if (ch >= '\u0000' && ch <= '\u001F') {
                         String ss = Integer.toHexString(ch);
                         sb.append("\\u");
                         for (int k = 0; k < 4 - ss.length(); k++) {
                             sb.append('0');
                         }
                         sb.append(ss.toUpperCase());
                     } else {
                         sb.append(ch);
                     }
            }
        }
        return sb.toString();
    }
    
    private static String array2json(Object[] array) {  
        StringBuilder json = new StringBuilder();  
        json.append("[");  
        if (array != null && array.length > 0) {  
            for (Object obj : array) {  
                json.append(object2json(obj));  
                json.append(",");  
            }
            json.setCharAt(json.length() - 1, ']');  
        } else {  
            json.append("]");  
        }  
        return json.toString();
    }
    
    private static String list2json(List<?> list) {  
        StringBuilder json = new StringBuilder();  
        json.append("[");  
        if (list != null && list.size() > 0) {  
            for (Object obj : list) {  
                json.append(object2json(obj));  
                json.append(",");  
            }  
            json.setCharAt(json.length() - 1, ']');  
        } else {  
            json.append("]");  
        }  
        return json.toString();
    }
    
    private static String map2json(Map<?, ?> map) {  
        StringBuilder json = new StringBuilder();  
        json.append("{");  
        if (map != null && map.size() > 0) {  
            for (Object key : map.keySet()) {  
                json.append(object2json(key));  
                json.append(":");  
                json.append(object2json(map.get(key)));  
                json.append(",");  
            }  
            json.setCharAt(json.length() - 1, '}');  
        } else {  
            json.append("}");  
        }  
        return json.toString();  
    
    }
    
    private static String GsonAry2json(JsonArray garry){
    	 StringBuilder json = new StringBuilder();  
         json.append("[");  
         if (garry != null && garry.size() > 0) {  
             for (JsonElement obj : garry) {
            	 if(obj.isJsonArray()){
            		 json.append(GsonAry2json(obj.getAsJsonArray())); 
            	 }else if(obj.isJsonObject()){
            		 json.append(Gson2json(obj.getAsJsonObject())); 
            	 }else if(obj.isJsonPrimitive()){
            		 json.append(object2json(obj.getAsString())); 
            	 }
                  
                 json.append(",");  
             }  
             json.setCharAt(json.length() - 1, ']');  
         } else {  
             json.append("]");  
         }  
         return json.toString();
    }
    
    private static String Gson2json(JsonObject gson) {  
        StringBuilder json = new StringBuilder();  
        json.append("{");  
        if (gson != null ) {  
        	Set<Entry<String, JsonElement>> keys = gson.entrySet();
			Set<Entry<String, JsonElement>> cloneKeys = new HashSet<Map.Entry<String,JsonElement>>();
			cloneKeys.addAll(keys);
			if(cloneKeys!=null&&!cloneKeys.isEmpty()) {
				for (Entry<String, JsonElement> key : cloneKeys) {
					JsonElement value = key.getValue();
					json.append(object2json(key.getKey()));
					json.append(":");
					if(value.isJsonArray()){
						json.append(object2json(value.getAsJsonArray())); 
					}else if(value.isJsonObject()){
						json.append(object2json(value.getAsJsonObject())); 
					}else{
						json.append(object2json(value.getAsString()));
					}
					 
		            json.append(",");  
				}
	            
	            json.setCharAt(json.length() - 1, '}');  
			}else {
				json.append("}"); 
			}
			
        } else {  
            json.append("}");  
        }  
        return json.toString();  
    }
    
    
    private static String set2json(Set<?> set) {  
        StringBuilder json = new StringBuilder();  
        json.append("[");  
        if (set != null && set.size() > 0) {  
            for (Object obj : set) {  
                json.append(object2json(obj));  
                json.append(",");  
            }  
            json.setCharAt(json.length() - 1, ']');  
        } else { 
            json.append("]");
        }  
        return json.toString();  
    }
    
    private static String bean2json(Object bean) {  
        StringBuilder json = new StringBuilder();  
        json.append("{");  
        PropertyDescriptor[] props = null;  
        try {  
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();  
        } catch (IntrospectionException e) {}  
        if (props != null) {  
            for (int i = 0; i < props.length; i++) {  
                try {  
                    String name = object2json(props[i].getName());  
                    String value = object2json(props[i].getReadMethod().invoke(bean));  
                    json.append(name);  
                    json.append(":");  
                    json.append(value);  
                    json.append(",");  
                } catch (Exception e) {}  
            }  
            json.setCharAt(json.length() - 1, '}');  
         } else {  
             json.append("}");  
        }  
         return json.toString();  
    }
    /**
     * 去除 null
     * @param json
     */
    public static void deleteNull(Object json) {
		if (json instanceof JSONArray) {
			JSONArray arr = (JSONArray) json;
			for (Object obj : arr) {
				deleteNull(obj);
			}
		} else if (json instanceof JSONObject) {
			JSONObject jo = (JSONObject) json;
			Set<String> keys = jo.keySet();
			String[] array = keys.toArray(new String[keys.size()]);
			for (String key : array) {
				Object value = jo.get(key);

				jo.remove(key);

				deleteNull(value);
				if(!value.toString().equals("null")){
					jo.put(key, value);
				}

			}
		}
	}
    
    /**
     * 将 null 设 空字符串
     * @param json
     */
    public static void setNullToEmpty(Object json) {
		if (json instanceof JSONArray) {
			JSONArray arr = (JSONArray) json;
			for (Object obj : arr) {
				setNullToEmpty(obj);
			}
		} else if (json instanceof JSONObject) {
			JSONObject jo = (JSONObject) json;
			Set<String> keys = jo.keySet();
			String[] array = keys.toArray(new String[keys.size()]);
			for (String key : array) {
				Object value = jo.get(key);

				jo.remove(key);

				setNullToEmpty(value);
				if(value.toString().equals("null")){
					jo.put(key, "");
				}else{
					jo.put(key, value);
				}

			}
		}
	}
}

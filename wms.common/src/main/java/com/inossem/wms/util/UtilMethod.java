package com.inossem.wms.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UtilMethod {

	@SuppressWarnings("unchecked")
	public static Method getSetMethod(Class objectClass, String fieldName) {
		try {
			Class[] parameterTypes = new Class[1];
			Field field = objectClass.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			StringBuffer sb = new StringBuffer();
			sb.append("set");
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			Method method = objectClass.getMethod(sb.toString(), parameterTypes);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static Object getValue(Object dto, String fieldName) {
		try {
			Method[] m = dto.getClass().getMethods();
			for (int i = 0; i < m.length; i++) {
				if (("get" + fieldName).toLowerCase().equals(m[i].getName().toLowerCase())) {
					return m[i].invoke(dto);
				}
			}

		} catch (Exception e) {

		}
		return null;
	}

}

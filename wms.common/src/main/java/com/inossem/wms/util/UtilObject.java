package com.inossem.wms.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UtilObject {
	public static String getStringOrEmpty(Object obj) {
		return UtilString.getStringOrEmptyForObject(obj);
	}

	public static String getStringOrNull(Object obj) {
		return UtilString.getStringOrNullForObject(obj);
	}

	public static Long getLongOrNull(Object obj) {
		if (obj == null) {
			return null;
		} else {
			try {
				return Long.parseLong(obj.toString().trim());
			} catch (Exception e) {
				return null;
			}
			
		}
	}

	public static Integer getIntegerOrNull(Object obj) {
		try {
			if (obj == null) {
				return null;
			} else {
				String str = obj.toString().trim();
				if (str.length() == 0 || "null".equalsIgnoreCase(str)) {
					return null;
				} else {
					Double valueDouble = Double.parseDouble(obj.toString().trim());
					return valueDouble.intValue();
				}
			}
		} catch (

		Exception ex) {
			return null;
		}
	}

	public static int getIntOrZero(Object obj) {
		if (null == obj) {
			return 0;
		} else {
			try {
				String str = obj.toString().trim();
				if (str.length() == 0 || "null".equalsIgnoreCase(str)) {
					return 0;
				} else {
					return Integer.parseInt(str);
				}
			} catch (Exception e) {
				return 0;
			}
		}
	}

	/**
	 * 将Object的字符串转换成BigDecimal
	 */
	public static BigDecimal getBigDecimalOrZero(Object obj) {
		BigDecimal ret = BigDecimal.ZERO;
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				ret = (BigDecimal) obj;
			} else if (obj instanceof String) {
				String str = obj.toString().trim();
				if (str.length() > 0) {
					ret = new BigDecimal(str);
				}
			} else if (obj instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) obj);
			} else if (obj instanceof Number) {
			//	ret = new BigDecimal(((Number) obj).doubleValue());
				ret = new BigDecimal(((Number) obj).toString());
			} else {
				throw new ClassCastException(
						"Not possible to coerce [" + obj + "] from class " + obj.getClass() + " into a BigDecimal.");
			}
		}
		ret = UtilBigDecimal.getBigDecimalStripTrailingZeros(ret);
		ret = new BigDecimal(ret.toPlainString());
		return ret;
	}
	
	public static Byte getByteOrNull(Object obj) {
		if (obj == null) {
			return null;
		} else {

			try {
				return Byte.parseByte(obj.toString().trim());
			} catch (Exception e) {
				return null;
			}
			
		}
	}
	
}

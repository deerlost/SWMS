package com.inossem.wms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilNumber {
	
	
	public static boolean isNumber(String str) {

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 拆箱
	 * 
	 * @param b
	 * @return
	 */
	public static byte getByteOrZeroForByte(Byte b) {
		if (b == null) {
			return (byte) 0;
		} else {
			return b.byteValue();
		}
	}

	/**
	 * 拆箱
	 * 
	 * @param b
	 * @return
	 */
	public static int getIntOrZeroForInteger(Integer i) {
		if (i == null) {
			return 0;
		} else {
			return i.intValue();
		}
	}
}

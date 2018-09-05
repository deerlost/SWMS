package com.inossem.wms.util;

import java.math.BigDecimal;

public class UtilBigDecimal {
	/**
	 * 除去尾随零
	 * 
	 * @param date
	 * @return
	 */
	public static BigDecimal getBigDecimalStripTrailingZeros(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			return BigDecimal.ZERO;
		} else {
			return bigDecimal.stripTrailingZeros();
		}
	}

	/**
	 * 除去尾随零
	 * 
	 * @param date
	 * @return
	 */
	public static BigDecimal getBigDecimalStripTrailingZerosForString(String value) {
		if (value == null) {
			return BigDecimal.ZERO;
		} else {
			return new BigDecimal(value).stripTrailingZeros();
		}
	}
}

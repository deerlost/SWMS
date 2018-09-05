package com.inossem.wms.util;

import java.util.Calendar;
import java.util.Date;

public class UtilDateTime {
	public static Date getDate(Date date, int i) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, i);// 今天+1天

		return c.getTime();
	}
	
	

}

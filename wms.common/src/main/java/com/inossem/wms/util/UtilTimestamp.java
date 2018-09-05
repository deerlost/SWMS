package com.inossem.wms.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilTimestamp {
	private static final Logger logger = LoggerFactory.getLogger(UtilTimestamp.class);
	private static SimpleDateFormat sdfLongForChina = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	private static SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 * 时间戳转换成日期
	 */
	public static Date getDatetimeForStamp(Timestamp stamp) {
		Date temp = new Date();
		String str = sdfLongForChina.format(stamp);
		try {
			temp = sdfLong.parse(str);
		} catch (ParseException e) {
			logger.error("", e);
		}
		return temp;
	}
	
	public static String formartDate(Date date) {
		if(date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static String formartDate(Date date,String format) {
		if(date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
}

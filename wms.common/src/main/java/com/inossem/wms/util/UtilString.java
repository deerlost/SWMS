package com.inossem.wms.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.inossem.wms.constant.Const;
import com.inossem.wms.model.vo.MatCodeVo;

public class UtilString {

	/*
	 * 输入参数 输出删除 是String的所有方法
	 * 
	 * 
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(UtilString.class);

	public static final SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String STRING_EMPTY = "";
	public static final String STRING_NULL = "null";

	/**
	 * 字符串是否为空
	 */
	public static boolean isNullOrEmpty(String value) {

		if (value == null || STRING_NULL.equalsIgnoreCase(value.trim()) || STRING_EMPTY.equals(value.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 字符串转换成int
	 * 
	 * @param value
	 * @return
	 */
	public static int getIntForString(String value) {
		if (isNullOrEmpty(value)) {
			return -1;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				return -1;
			}
		}
	}

	/**
	 * 
	 * 长字符串转换成日期
	 */
	public static Date getDateTimeForString(String value) {
		if (isNullOrEmpty(value)) {
			return null;
		} else {
			Date date;
			try {
				date = sdfLong.parse(value.toString());
			} catch (ParseException e) {
				logger.error(STRING_EMPTY, e);
				date = new Date();
			}
			return date;
		}
	}

	/**
	 * 
	 * 短字符串转换成日期
	 */
	public static Date getDateForString(String value) {
		if (isNullOrEmpty(value)) {
			return null;
		}

		Date date1 = new Date();
		try {
			date1 = sdfShort.parse(value.toString());
		} catch (ParseException e) {
			logger.error(STRING_EMPTY, e);
		}
		return date1;
	}

	/**
	 * 字符串转化为日期
	 */
	public static Date getDateForString(String value, String format) {
		Date date = null;
		if (isNullOrEmpty(value)) {
			return null;
		}
		try {
			if (null == format || STRING_EMPTY.equals(format)) {
				format = "yyyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);
		} catch (Exception e) {
			logger.error(STRING_EMPTY, e);
		}
		return date;
	}

	/**
	 * 返回非空字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getStringOrEmptyForObject(Object obj) {
		if (obj == null) {
			return STRING_EMPTY;
		} else if (obj instanceof String) {
			String s = (String) obj;
			if (isNullOrEmpty(s)) {
				return STRING_EMPTY;
			} else {
				return s;
			}
		} else if (obj instanceof BigDecimal) {
			BigDecimal bigDecimal = (BigDecimal) obj;
			return bigDecimal.toPlainString();
		} else if (obj instanceof Date) {
			Date date = (Date) obj;
			return sdfShort.format(date);
		} else if (obj instanceof Integer) {
			Integer integer = (Integer) obj;
			return integer.toString();
		} else if (obj instanceof Long) {
			Long l = (Long) obj;
			return l.toString();
		} else if (obj instanceof Double) {
			Double d = (Double) obj;
			return d.toString();
		} else if (obj instanceof Byte) {
			Byte b = (Byte) obj;
			return b.toString();
		} else {
			return obj.toString();
		}
	}

	/**
	 * 返回可空字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getStringOrNullForObject(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof String) {
			String s = (String) obj;
			if (isNullOrEmpty(s)) {
				return null;
			} else {
				return s;
			}
		} else if (obj instanceof BigDecimal) {
			BigDecimal bigDecimal = (BigDecimal) obj;
			return bigDecimal.toPlainString();
		} else if (obj instanceof Date) {
			Date date = (Date) obj;
			return sdfShort.format(date);
		} else if (obj instanceof Integer) {
			Integer integer = (Integer) obj;
			return integer.toString();
		} else if (obj instanceof Long) {
			Long l = (Long) obj;
			return l.toString();
		} else if (obj instanceof Double) {
			Double d = (Double) obj;
			return d.toString();
		} else if (obj instanceof Byte) {
			Byte b = (Byte) obj;
			return b.toString();
		} else {
			return obj.toString();
		}
	}

	/**
	 * 返回非空短日期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getShortStringForDate(Date date) {
		if (date == null) {
			return STRING_EMPTY;
		} else {
			return sdfShort.format(date);
		}
	}

	/**
	 * 返回非空长日期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getLongStringForDate(Date date) {
		if (date == null) {
			return STRING_EMPTY;
		} else {
			return sdfLong.format(date);
		}
	}

	/**
	 * 返回非空定点数字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringForBigDecimal(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			return STRING_EMPTY;
		} else {
			return bigDecimal.toPlainString();
		}
	}

	/**
	 * 返回空定点数字符串
	 * 
	 * @param Byte
	 * @return
	 */
	public static String getStringOrNullForByte(Byte b) {
		if (b == null) {
			return null;
		} else {
			return b.toString();
		}
	}

	/**
	 * 返回空字符串
	 * 
	 * @param Byte
	 * @return
	 */
	public static String getStringOrEmptyForByte(Byte b) {
		if (b == null) {
			return STRING_EMPTY;
		} else {
			return b.toString();
		}
	}

	/**
	 * 除去尾随零,非科学计数法
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringStripTrailingZeros(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			return "0";
		} else {
			return bigDecimal.stripTrailingZeros().toPlainString();
		}
	}

	/**
	 * 保留两位小数
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringRetainDecimals(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			return "0";
		} else {
			return bigDecimal.setScale(2).toPlainString();
		}
	}

	/**
	 * 返回错误信息 方法
	 * 
	 * @param isSap
	 *            是否sap返回结果
	 * @param errorString
	 *            原返回的错误信息
	 * @return
	 */
	public static String getErrorString(boolean isSap, String errorString) {
		if (isSap) {
			return String.format(Const.SAP_API_MSG_FORMAT, Const.SAP_API_MSG_PREFIX, errorString);
		} else {
			return errorString;
		}
	}

	/**
	 * 判断数组是否存在
	 * 
	 * @param array
	 * @return
	 */
	public static boolean arrayHasLength(Object[] array) {
		return (array != null && array.length > 0);
	}

	/**
	 * 截取物料编码
	 * 
	 * @author 刘宇 2018.02.05
	 * @param matCode
	 * @return
	 */
	public static List<MatCodeVo> cutOutMatCode(String matCode) {
		List<MatCodeVo> utilMatCodes = new ArrayList<MatCodeVo>();
		if(!StringUtils.hasText(matCode)){
			return utilMatCodes;
		}
		String str2 = matCode.replaceAll("，", ",");// 将物料编码字符串中文逗号替换成英文逗号
		String str3 = str2.replaceAll("-", "~");// 将物料编码字符串-逗号替换成~
		int count = 0;// 英文逗号数量
		Pattern p = Pattern.compile(",");
		Matcher m = p.matcher(str3);
		while (m.find()) {
			count++;
		}
		int countLine = 0;// ~线数量
		p = Pattern.compile("~");
		m = p.matcher(str3);
		while (m.find()) {
			countLine++;
		}

		
		int countArray = count + 1 - countLine;// 物料编码数组长度
		String[] strs = str3.split(","); // 英文逗号分隔字符串
		int i = 0;
		String[] strIn = new String[countArray];
		if (countLine != 0 && count != 0 && countLine <= count) {// 区间哥单个物料编码都存在
			for (String string : strs) {

				int countLines = 0;
				p = Pattern.compile("~");
				m = p.matcher(string);
				while (m.find()) {
					countLines++;
				}
				if (countLines == 0) {
					strIn[i++] = string;
				} else if (countLines != 0) {
					MatCodeVo utilMatCode = new MatCodeVo();
					String begin = string.substring(0, string.indexOf("~"));
					String end = string.substring(string.indexOf("~") + 1, string.length());
					utilMatCode.setBegin(begin);
					utilMatCode.setEnd(end);
					utilMatCode.setStrings(strIn);
					utilMatCodes.add(utilMatCode);
				}

			}
		} else if (countLine != 0 && countLine > count) {// 只有区间物料编码
			for (String string : strs) {
				MatCodeVo utilMatCode = new MatCodeVo();
				String begin = string.substring(0, string.indexOf("~"));
				String end = string.substring(string.indexOf("~") + 1, string.length());
				strs[i++] = begin;
				utilMatCode.setBegin(begin);
				utilMatCode.setEnd(end);
				utilMatCode.setStrings(strs);
				utilMatCodes.add(utilMatCode);
			}

		} else if (countLine == 0 && UtilString.isNullOrEmpty(matCode) == false) {// 只有单个存在的物料编码
			for (String string : strs) {
				strIn[i++] = string;
			}
			MatCodeVo utilMatCode = new MatCodeVo();
			utilMatCode.setBegin("-1");
			utilMatCode.setEnd("-1");
			utilMatCode.setStrings(strIn);
			utilMatCodes.add(utilMatCode);
		}

		return utilMatCodes;
	}
	
	/**
	 * 将Object的字符串转换成BigDecimal
	 */
	public static BigDecimal objToBigDecimal(Object value) {
		BigDecimal ret = null;
		if (value != null && !"".equals(value)) {
			value = value.toString();
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()
						+ " into a BigDecimal.");
			}
		} else {
			ret = BigDecimal.ZERO;
		}
		// ret = ret.setScale(2,BigDecimal.ROUND_HALF_UP);
		return ret;
	}
	
	/**
     * 拆分字符串
     * @param str       待拆分的字符串
     * @param separator 分隔符
     * @return          String[]
     */
    public static String[] splitStr(String str, String separator){
    	
    	if (isNullOrEmpty(str)) { return null; }
    	
		String[] splitArray = str.split(separator);
		
		if (splitArray != null && splitArray.length > 0) {
			int len = splitArray.length;
			String[] retArray = new String[len];
			for (int i = 0; i < len; i++) {
				if (!isNullOrEmpty((splitArray[i]))) {
					retArray[i] = splitArray[i];
				}
			}
			return retArray;
		}
		return null;
    }
    
    /**
	 * 过滤任意(script,html,style)标签符,返回纯文本
	 * 
	 * @param inputString
	 * @return
	 */
	public static String HtmlToText(Object inputString) {
		if (null == inputString) {
			return null;
		}
		String htmlStr = inputString.toString(); // 含html标签的字符串
		String textStr = "";
		Pattern p_script;
		java.util.regex.Matcher m_script;
		Pattern p_style;
		java.util.regex.Matcher m_style;
		Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());

		}
		return textStr;// 返回文本字符串

	}
    
    /**
	 * 将字符串转换num位字符串，不足num位的左补0
	 * @param value
	 * @param num
	 * @return
	 */
	public static String toLimitNumDigit(String value, int num) {
		for(int i = 0; i < num; i++) {
			if(value.length() < num) {
				value = "0" + value;
			}
		}
		return value;
	}
	
	/**
     * 得到系统当前日期时间字符串
     * 
     * @return 当前日期时间
     */
    public static String getNowToShortString() {
        return getShortStringForDate(Calendar.getInstance().getTime());
    }
    
    /**
     * 得到系统当前日期时间
     * 
     * @return 当前日期时间
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }
}

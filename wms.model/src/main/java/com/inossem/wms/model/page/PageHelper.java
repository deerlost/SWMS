package com.inossem.wms.model.page;

import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;

public class PageHelper {

	public static final String SELECT = "select";
	public static final String FROM = "from";
	public static final String ORDER = "order";
	public static final String GROUP = "group";
	public static final String DISTINCT = "distinct";
	public static final String UNION = "union";

	/**
	 * 获得最外层from的index 当前from和下一个from的index都大于零时 计算当前from之前的右括号不少于左括号
	 * 
	 * @param text
	 * @return
	 */
	public static int topFrom(String text) {
		int preFrom = -1;
		int currentFrom = firstKeyword(text, FROM, preFrom + 1);
		int nextFrom = firstKeyword(text, FROM, currentFrom + 1);
		char[] cs = text.toCharArray();
		int leftCnt = 0;
		int rightCnt = 0;
		while (currentFrom > 0 && nextFrom > 0) {
			// 统计当前from前的左右括号个数
			for (int i = preFrom + 1; i < currentFrom; i++) {
				switch (cs[i]) {
				case '(':
					++leftCnt;
					break;
				case ')':
					++rightCnt;
				default:
					break;
				}
			}
			// 右括号不小于左括号个数
			if (leftCnt <= rightCnt) {
				break;
			}
			preFrom = currentFrom;
			currentFrom = nextFrom;
			nextFrom = firstKeyword(text, FROM, currentFrom + 1);
		}
		return currentFrom;
	}

	/**
	 * 获取index之后的第一个关键字位置,小于零代表没有
	 * 
	 * @param text
	 * @param keyword
	 * @param index
	 * @return
	 */
	public static PageWord firstColumn(String text, String column, int index) {
		int i = text.indexOf(column, index);
		if (i < 0 || i >= text.length()) {
			return null;
		} else {
			if (i == 0) {
				if (i + column.length() >= text.length()) {
					return new PageWord(i, column.length(), column);
				} else if (isSeparator(text.charAt(i + column.length()))) {
					return new PageWord(i, column.length(), column);
				} else {
					return firstColumn(text, column, i + 1);
				}

			} else {
				char prechar = text.charAt(i - 1);
				if (isSeparator(text.charAt(i + column.length()))) {
					if (isSeparator(prechar) || prechar == '.') {
						int beginIndex = i;
						int endIndex = i + column.length();
						while (!PageHelper.isSeparator(text.charAt(beginIndex - 1)) && beginIndex >= 0) {
							beginIndex -= 1;
						}
						return new PageWord(beginIndex, endIndex - beginIndex, column);

					} else {
						return firstColumn(text, column, i + 1);
					}
				} else {
					return firstColumn(text, column, i + 1);
				}
			}
		}
	}

	/**
	 * 获取index之后的第一个关键字位置,小于零代表没有
	 * 
	 * @param text
	 * @param keyword
	 * @param index
	 * @return
	 */
	public static int firstKeyword(String text, String keyword, int index) {
		int i = text.indexOf(keyword, index);
		if (i < 0 || i >= text.length()) {
			return -1;
		} else {
			if ((i == 0 || isSeparator(text.charAt(i - 1)))
					&& (i + keyword.length() >= text.length() || isSeparator(text.charAt(i + keyword.length())))) {
				return i;
			} else {
				return firstKeyword(text, keyword, i + 1);
			}
		}
	}

	/**
	 * 获取index之前的最后一个关键字位置,小于零代表没有
	 * 
	 * @param text
	 * @param keyword
	 * @param index
	 * @return
	 */
	public static int lastKeyword(String text, String keyword, int index) {
		int i = text.lastIndexOf(keyword, index);
		if (i < 0 || i >= text.length()) {
			return -1;
		} else {
			if ((i == 0 || isSeparator(text.charAt(i - 1)))
					&& (i + keyword.length() >= text.length() || isSeparator(text.charAt(i + keyword.length())))) {
				return i;
			} else {
				return lastKeyword(text, keyword, i - 1);
			}
		}
	}

	/**
	 * 判断是否分隔符[空字符,括号]
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isSeparator(char c) {
		if (c <= ' ') {
			return true;
		} else {
			switch (c) {
			case '(':
				return true;
			// break;
			case ')':
				return true;
			case ',':
				return true;
			// break;
			default:
				return false;
			// break;
			}
		}
	}

	/**
	 * 转换为驼峰
	 * 
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
				if (underscore)
					s = s.toUpperCase();
				underscore = false;
			}
			result.append(s);
		}
		return result.toString();
	}

	public static int getPageSizeFromJSON(JSONObject obj) {
		try {
			return obj.getInt("page_size");
		} catch (Exception e) {
			// e.printStackTrace();
			return -1;
		}
	}

	public static int getPageIndexFromJSON(JSONObject obj) {
		try {
			return obj.getInt("page_index");
		} catch (Exception e) {
			// e.printStackTrace();
			return -1;
		}
	}

	public static int getTotalCountFromJSON(JSONObject obj) {
		try {
			return obj.getInt("total");
		} catch (Exception e) {
			// e.printStackTrace();
			return -1;
		}
	}

	public static boolean getSortAscendFromJSON(JSONObject obj) {
		try {
			return obj.getBoolean("sort_ascend");
		} catch (Exception e) {
			// e.printStackTrace();
			return true;
		}
	}

	public static String getSortColumnFromJSON(JSONObject obj) {
		try {
			return obj.getString("sort_column");
		} catch (Exception e) {
			// e.printStackTrace();
			return "";
		}
	}

	public static PageParameter getParameterFromJSON(JSONObject obj) {
		PageParameter parameter = new PageParameter();

		try {
			if (obj.containsKey("page_size")) {
				parameter.setPageSize(obj.getInt("page_size"));
			}
			if (obj.containsKey("page_index")) {
				parameter.setPageIndex(obj.getInt("page_index"));
			}
			if (obj.containsKey("total")) {
				parameter.setTotalCount(obj.getInt("total"));
			}
			if (obj.containsKey("sort_ascend")) {
				parameter.setSortAscend(obj.getBoolean("sort_ascend"));
			}
			if (obj.containsKey("sort_column")) {
				parameter.setSortColumn(obj.getString("sort_column"));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return parameter;
	}
}

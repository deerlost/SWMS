package com.inossem.wms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;

import com.inossem.wms.exception.ExcelCellTypeException;
import com.inossem.wms.model.ExcelCellType;

/**
 * 报表新增生成打印基类
 * 
 * @author 高海涛
 */
public abstract class UtilExcel {

	private static final String EXCEL_PATH = "/excel";
	private static final String TYPE_XLS = ".xls";

	public static final String CELL_TYPE_NUM = "NUM";
	public static final String CELL_TYPE_DATE = "DATE";
	public static final String CELL_TYPE_DOUBLE = "DOUBLE";

	/** 校验数据是否存在 **/
	public static final String VALIDATION_DATA_EXISTS = "EXISTS";
	public static final String VALIDATION_DATA_NOTEXISTS = "NOTEXISTS";

	/**
	 * 把Excel文件数据转换为List
	 * 
	 * @date 2017年11月6日
	 * @author 高海涛
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getExcelDataList(String url) throws Exception {
		return getExcelDataList(url, null, null, null, 0, null);
	}

	public static List<Map<String, Object>> getExcelDataList(String url, List<String> scientificNotationList)
			throws Exception {
		return getExcelDataList(url, scientificNotationList, null, null, 0, null);
	}

	public static List<Map<String, Object>> getExcelDataList(String url, List<String> scientificNotationList,
			Map<String, String> titleMap) throws Exception {
		return getExcelDataList(url, scientificNotationList, titleMap, null, 0, null);
	}

	public static List<Map<String, Object>> getExcelDataList(String url, List<String> scientificNotationList,
			Map<String, String> titleMap, Map<String, ExcelCellType> checkData) throws Exception {
		return getExcelDataList(url, scientificNotationList, titleMap, null, 0, null);
	}

	public static List<Map<String, Object>> getExcelDataList(String url, List<String> scientificNotationList,
			Map<String, String> titleMap, Map<String, ExcelCellType> checkData,
			Map<String, List<String>> multValidation) throws Exception {
		return getExcelDataList(url, null, titleMap, checkData, 0, multValidation);
	}

	public static Map<String, List<Map<String, Object>>> getMultExcelDataList(String url,
			List<String> scientificNotationList, List<Map<String, String>> titleMap,
			Map<String, ExcelCellType> checkData, int sheetIndex, List<Map<String, List<String>>> multValidation)
			throws Exception {
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		for (int i = 0; i < sheetIndex; i++) {
			List<Map<String, Object>> list = getExcelDataList(url, scientificNotationList, titleMap.get(i), checkData,
					i, multValidation.get(i));
			map.put(String.valueOf(i), list);
		}
		return map;
	}

	private static List<Map<String, Object>> getExcelDataList(String url, List<String> scientificNotationList,
			Map<String, String> titleMap, Map<String, ExcelCellType> checkData, int sheetIndex,
			Map<String, List<String>> multValidation) throws Exception {
		if ("".equals(url)) {
			throw new Exception("文件路径为空!");
		}

		InputStream inputStream = new FileInputStream(url);
		POIFSFileSystem pOIFSFileSystem = new POIFSFileSystem(inputStream);
		Workbook workbook = new HSSFWorkbook(pOIFSFileSystem);

		String[] title = getExcelTitleKey(workbook, titleMap, sheetIndex);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Sheet sheet = (HSSFSheet) workbook.getSheetAt(sheetIndex);
		int rowNum = sheet.getLastRowNum();

		String lc_constr = "";
		for (int i = 1; i <= rowNum; i++) {

			Map<String, Object> multValidationValue = new HashMap<String, Object>();
			List<String> multValidationTitle = null;

			if (multValidation != null) {
				multValidationTitle = new ArrayList<String>();

				Set<String> set = multValidation.keySet();
				String[] t = null;
				for (String key : set) {
					t = key.split("-");
					for (int m = 0; m < t.length; m++) {
						multValidationTitle.add(t[m]);
					}

				}
			}

			Map<String, Object> map = new HashMap<String, Object>();

			lc_constr = "";
			for (int j = 0; j < title.length; j++) {
				String value = getCellFormatValue(sheet.getRow(i).getCell(j));

				if (multValidationTitle != null) {
					if (multValidationTitle.contains(title[j])) {
						multValidationValue.put(title[j], value);
					}
				}

				if (scientificNotationList != null) {
					for (int k = 0; k < scientificNotationList.size(); k++) {
						if (title[j].equals(scientificNotationList.get(k))) {
							if (value.indexOf("E") > 0 || value.indexOf("e") > 0) {
								BigDecimal bd = new BigDecimal(value);
								value = bd.toPlainString();
							}
						}
					}

				}

				if (checkData != null) {
					if (checkData.containsKey(title[j])) {
						ExcelCellType excelCellType = checkData.get(title[j]);

						if (excelCellType.isNotNull()) {
							if ("".equals(value) || value == null) {
								throw new ExcelCellTypeException("Excel文档中的 " + title[j] + " 存在空值!");
							}
						}

						if (!"".equals(value) && value != null) {
							if (excelCellType.getLength() > 0) {
								if (excelCellType.getLength() < value.length()) {
									throw new ExcelCellTypeException("Excel文档中的 " + title[j] + " 存在长度过长的内容 - " + value);
								}
							}

							if (!"".equals(excelCellType.getType()) && excelCellType.getType() != null) {
								switch (excelCellType.getType()) {
								case CELL_TYPE_NUM:
									Pattern pattern = Pattern.compile("[0-9]*");
									Matcher isNum = pattern.matcher(value);
									if (!isNum.matches()) {
										throw new ExcelCellTypeException(
												"Excel文档中的 " + title[j] + " 只允许为数字 - " + value);
									}
									break;
								case CELL_TYPE_DOUBLE:
									boolean isDouble = value.matches("-?[0-9]+.*[0-9]*");
									if (!isDouble) {
										throw new ExcelCellTypeException(
												"Excel文档中的 " + title[j] + " 只允许为数字 - " + value);
									}
									break;
								case CELL_TYPE_DATE:
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									try {
										format.setLenient(false);
										format.parse(value);
									} catch (ParseException e) {
										throw new ExcelCellTypeException(
												"Excel文档中的 " + title[j] + " 只允许为日期[YYYY-MM-DD]  - " + value);
									}
									break;
								default:
									break;
								}
							}

							if (excelCellType.isNeedValidation()) {

								if (excelCellType.getValidationData() != null) {
									Map<String, List<String>> dataMap = excelCellType.getValidationData();
									if (dataMap.containsKey(VALIDATION_DATA_EXISTS)) {
										List<String> data = dataMap.get(VALIDATION_DATA_EXISTS);
										if (data.size() > 0) {
											if (!data.contains(value)) {
												throw new ExcelCellTypeException(
														"Excel文档中的 " + title[j] + " 存在与WMS系统数据不匹配的内容 - " + value);
											}
										}

									}
								}
							}

						}
					}
				}
				map.put(title[j], value);
				lc_constr = lc_constr + value;
			}

			if (multValidation != null) {

				Iterator<Map.Entry<String, List<String>>> entries = multValidation.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String, List<String>> entry = entries.next();

					String key = entry.getKey();
					String keyValue = "";

					String[] keyArr = key.split("-");
					for (String k : keyArr) {
						if (!"".equals(keyValue)) {
							keyValue = keyValue + "-";
						}
						keyValue = keyValue + multValidationValue.get(k);
					}

					List<String> checklist = entry.getValue();
					String type = checklist.get(checklist.size() - 1);

					if (VALIDATION_DATA_EXISTS.equals(type)) {
						checklist.remove(checklist.size() - 1);

						if (!checklist.contains(keyValue)) {
							throw new ExcelCellTypeException("Excel文档中的 " + key + " 存在与WMS系统数据不匹配的内容 - " + keyValue);
						}

					} else if (VALIDATION_DATA_NOTEXISTS.equals(type)) {
						checklist.remove(checklist.size() - 1);

						if (checklist.contains(keyValue)) {
							throw new ExcelCellTypeException("Excel文档中的 " + key + " 在WMS系统数据中已存在- " + keyValue);
						}
					}
				}
			}

			if (lc_constr.equals("")) {
				break;
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @date 2017年11月6日
	 * @author 高海涛
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String getCellFormatValue(Cell cell) {
		String value = null;
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {

			// 如果当前Cell的Type为NUMERIC
			case Cell.CELL_TYPE_NUMERIC:
				value = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (DateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式
					// 带时分秒的：
					// value = cell.getDateCellValue().toLocaleString();
					// 不带带时分秒的:
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					value = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					value = String.valueOf(cell.getStringCellValue());
				}
				break;
			}

			// 如果当前Cell的Type为STRIN
			case Cell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				value = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值

			default:
				value = null;
			}
		}
		if (value == null) {
			value = "";
		} else {
			value.trim();
		}
		return value;
	}

	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @date 2017年11月6日
	 * @author 高海涛
	 * @param workbook
	 * @return
	 * @throws Exception
	 */
	private static String[] getExcelTitleKey(Workbook workbook, Map<String, String> titleMap, int sheetIndex)
			throws Exception {
		Sheet sheet = (HSSFSheet) workbook.getSheetAt(sheetIndex);
		Row row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			title[i] = getCellFormatValue(row.getCell(i));
		}
		if (titleMap != null) {
			title = titleChange(title, titleMap);
		}

		return title;
	}

	private static String[] titleChange(String[] title, Map<String, String> titleMap) {
		for (int i = 0; i < title.length; i++) {
			title[i] = titleMap.get(title[i]);
		}
		return title;
	}

	/**
	 * 获取Excel报表的URL
	 * 
	 * @param <T>
	 * @param title
	 * @param entityList
	 * @param relateionList
	 * @param orderList
	 * @return
	 * @throws Exception
	 */
	public static <T> String getExcelRopertUrl(String title, List<Map<String, Object>> entityList,
			Map<String, String> relationMap, List<String> orderList, String root) throws Exception {
		if (StringUtils.isBlank(title) || CollectionUtils.isEmpty(relationMap)) {
			throw new Exception("传入的参数存在空值");
		}
		return getExcelRopertUrl(title, getTransformDataList(entityList, relationMap), orderList, root);
	}

	/**
	 * 获取Excel报表的URL 组装关系后
	 * 
	 * @param list
	 * @param title
	 * @param orderList
	 *            表头显示顺序的list
	 * @return
	 * @throws Exception
	 */
	public static String getExcelRopertUrl(String title, List<Map<String, Object>> list, List<String> orderList,
			String root) throws Exception {
		if (StringUtils.isBlank(title)) {
			throw new Exception("传入的参数存在空值");
		}
		Workbook workbook = wrapDataList2Workbook(title, list, orderList);
		// 创建文件，并获取路径
		String finPath = getWorkbookFinalUrl(workbook, root);
		return formatPath2Url(finPath);
	}

	/**
	 * 把报表格式的数据封装成工作簿
	 * 
	 * @param list
	 * @param title
	 * @return
	 */
	private static Workbook wrapDataList2Workbook(String title, List<Map<String, Object>> list,
			List<String> orderList) {

		Workbook workbook = new HSSFWorkbook();
		// 创建Excel文档
		createWorkbook(workbook, title);
		// 创建一个工作页
		createSheet(workbook, title);

		createTitleRow(workbook, list, orderList);

		createDataRow(workbook, list, orderList);
		// 设置工作页样式
		setSheetStyle(workbook, list);
		// 设置工作页打印设置
		setSheetPrintSetup(workbook, list);

		// 设置文档样式
		setWorkbookStyle(workbook, list, orderList);

		return workbook;
	}

	/**
	 * 创建文档
	 * 
	 * @param workbook
	 * @return
	 */
	private static void createWorkbook(Workbook workbook, String title) {
		workbook.createName().setNameName(title);
	}

	/**
	 * 创建工作页
	 * 
	 * @param workbook
	 * @return
	 */
	private static void createSheet(Workbook workbook, String title) {
		workbook.createSheet(title);
	}

	/**
	 * 创建标题行
	 * 
	 * @param workbook
	 * @param list
	 * @return
	 */
	private static void createTitleRow(Workbook workbook, List<Map<String, Object>> list, List<String> orderList) {
		Sheet sheet = workbook.getSheetAt(0);
		Row titleRow = sheet.createRow(0);// 下标为0的行开始
		titleRow.setHeight((short) 540);
		String[] titleArray = getTitleArray(list, orderList);
		CellStyle style = getTitleCellStyle(workbook);
		for (int i = 0; i < titleArray.length; i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(new HSSFRichTextString(titleArray[i]));
		}

	}

	/**
	 * 创建数据行
	 * 
	 * @param workbook
	 * @param list
	 * @return
	 */
	private static void createDataRow(Workbook workbook, List<Map<String, Object>> list, List<String> orderList) {
		Sheet sheet = workbook.getSheetAt(0);
		String[] titleArray = getTitleArray(list, orderList);
		CellStyle style = getDataCellStyle(workbook);
		for (int i = 0; i < list.size(); i++) {
			// 创建一行
			Row dataRow = sheet.createRow(i + 1);
			dataRow.setHeight((short) 540);
			// 得到要插入的每一条记录，在一行内循环创建
			for (int j = 0; j < titleArray.length; j++) {
				sheet.setColumnWidth(j, 3000);
				String key = orderList.get(j);
				String value = UtilObject.getStringOrEmpty(list.get(i).get(key));
				Cell cell = dataRow.createCell(j);
				// 根据key判断value是否是数值
				Boolean isNum = false;
                if (key.contains("出库数量") || key.contains("入库数量") || key.contains("结余库存") || key.contains("移动平均价") || key.contains("结余库存") || key.contains("库龄")|| key.contains("容量")|| key.contains("使用率")
                		|| key.contains("库存数量") || key.contains("库存金额")|| key.contains("金额") || key.contains("单价")|| key.contains("数量")|| key.contains("笔数") || key.contains("交货数量")||key.contains("初期库存")) {
                    isNum = true;
                } else {
                	isNum = false;
                }
               // 如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置value的类型为数值类型
                if (isNum) {
                	// 保留三位小数点    
                    style.setDataFormat(HSSFDataFormat.getBuiltinFormat("###,###,##0.000"));            
                    // 设置单元格格式
                    cell.setCellStyle(style);
                    // 设置单元格内容为double类型
                    cell.setCellValue(Double.parseDouble(value));
                } else {
                	// 文本模式
            		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                	// 设置单元格格式
                	cell.setCellStyle(style);
                    // 设置单元格内容为字符型
                	cell.setCellValue(value);
                }
			}
		}
	}

	/**
	 * 获取标题行样式
	 * 
	 * @param workbook
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static CellStyle getTitleCellStyle(Workbook workbook) {
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		HSSFDataFormat format = (HSSFDataFormat) workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));
		return style;
	}

	/**
	 * 获取数据行样式
	 * 
	 * @param workbook
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static CellStyle getDataCellStyle(Workbook workbook) {
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		// 生成另一个字体
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style.setFont(font);
		return style;
	}

	/**
	 * 设置工作页样式
	 * 
	 * @param workbook
	 * @param list
	 * @return
	 */
	private static void setSheetStyle(Workbook workbook, List<Map<String, Object>> list) {
		Sheet sheet = workbook.getSheetAt(0);
		sheet.setDefaultColumnWidth(20);// 默认列宽度
		sheet.setAutobreaks(true);// 自动翻页
	}

	/**
	 * 设置工作页打印设置
	 * 
	 * @param workbook
	 * @param list
	 * @return
	 */
	private static void setSheetPrintSetup(Workbook workbook, List<Map<String, Object>> list) {
		PrintSetup print = workbook.getSheetAt(0).getPrintSetup();
		print.setLandscape(true);// 横向
		print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);// A4纸张
		print.setFitWidth((short) 1);// 将所有列调整为一页
		print.setFitHeight((short) 0);// 将所有列调整为一页（0表示不设置页数）

		HSSFFooter footer = (HSSFFooter) workbook.getSheetAt(0).getFooter();
		footer.setCenter("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
	}

	/**
	 * 设置文档样式
	 * 
	 * @param workbook
	 * @param list
	 * @return
	 */
	private static void setWorkbookStyle(Workbook workbook, List<Map<String, Object>> list, List<String> orderList) {
		workbook.setPrintArea(0, 0, getTitleArray(list, orderList).length - 1, 0, list.size() + 2);// 设置打印区域
	}

	/**
	 * 获取标题数组
	 * 
	 * @param list
	 * @return
	 */
	private static String[] getTitleArray(List<Map<String, Object>> list, List<String> orderList) {
		String[] array = null;
		if (!CollectionUtils.isEmpty(orderList)) {
			array = new String[orderList.size()];
			int i = 0;
			for (String key : orderList) {
				array[i] = key;
				i++;
			}
		} else {
			Map<String, Object> map = list.get(0);
			array = new String[map.size()];
			int i = 0;
			for (String key : map.keySet()) {
				array[i] = key;
				i++;
			}
		}
		return array;
	}

	/**
	 * 获取工作簿所在目录
	 * 
	 * @param workbook
	 * @return
	 */
	private static String getWorkbookDirectory() {
		return EXCEL_PATH + File.separator + getDateFolder() + File.separator;
	}

	/**
	 * 获取工作簿名称
	 * 
	 * @param workbook
	 * @return
	 */
	private static String getWorkbookName(Workbook workbook) {
		return workbook.getNameAt(0).getNameName() + "_" + getFormatedDate() + TYPE_XLS;
	}

	/**
	 * 获取工作簿最终URL
	 * 
	 * @param workbook
	 * @return
	 */
	private static String getWorkbookFinalUrl(Workbook workbook, String root) throws Exception {
		String path = formatUrl2Path(root + getWorkbookDirectory());
		String nameString = getWorkbookName(workbook);
		createWorkbookFile(workbook, path, nameString);
		return path + nameString;
	}

	/**
	 * 创建工作簿文件
	 * 
	 * @param workbook
	 * @param url
	 * @throws Exception
	 */
	private static void createWorkbookFile(Workbook workbook, String path, String fileName) throws Exception {
		createWorkbookDirectory(workbook, path);
		OutputStream out = new FileOutputStream(path + fileName);
		workbook.write(out);
		if (out != null) {
			out.close();
			out = null;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param workbook
	 * @throws IOException
	 */
	private static void createWorkbookDirectory(Workbook workbook, String url) throws Exception {
		File file = new File(url);
		if (!file.isDirectory()) {
			FileUtils.forceMkdir(file);
		}
	}

	/**
	 * 获得"20150805"这种形式的日期
	 * 
	 * @return
	 */
	private static String getDateFolder() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * 获得"20150818173319"这种形式的日期
	 * 
	 * @return
	 */
	private static String getFormatedDate() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * 将真实路径分隔符转化成URL形式
	 * 
	 * @param path
	 * @return
	 */
	private static String formatPath2Url(String path) {
		return path.replace(File.separator, "/");
	}

	/**
	 * 将URL分隔符转化成真实路径形式
	 * 
	 * @param url
	 * @return
	 */
	private static String formatUrl2Path(String url) {
		return url.replace("/", File.separator);
	}

	/**
	 * 转换map
	 * 
	 * @param <T>
	 * @param entityList
	 * @param relationMap
	 * @return
	 * @throws Exception
	 */
	private static <T> List<Map<String, Object>> getTransformDataList(List<Map<String, Object>> entityList,
			Map<String, String> relationMap) throws Exception {
		// 1、遍历entityList
		// 2、根据relateionList生成Map<String, String>
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (Map<String, Object> instance : entityList) {
			map = new HashMap<String, Object>();
			for (String key : instance.keySet()) {
				map.put(relationMap.get(key), UtilString.getStringOrEmptyForObject(instance.get(key)));
			}
			list.add(map);
		}
		return list;
	}

}

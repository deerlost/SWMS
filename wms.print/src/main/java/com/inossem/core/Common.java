package com.inossem.core;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.cglib.core.internal.LoadingCache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @Package com.inossem.core
 * @ClassName：Common
 * @Description :
 * @anthor：王洋
 * @date：2018年6月19日 下午3:42:51 @版本：V1.0
 */
public class Common {

	private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private static final String[] RADICES = { "", "拾", "佰", "仟" };
	private static final String[] BIG_RADICES = { "", "万", "亿", "万亿" };

	public static String getRMB(BigDecimal money) throws Exception {
		return getRMB(money.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).longValue());
	}

	/**
	 * 获取大写的人名币的金额，单位精确到分
	 *
	 * @param money
	 *            人民币，单位：分
	 * @return 人民币大写的金额
	 * @throws Exception
	 */
	private static String getRMB(long money) throws Exception {
		StringBuilder result = new StringBuilder("");
		if (money == 0) {
			return "零元整";
		} else if (money < 0) {
			result.append("负");
			money = -money;
		}
		if (money > 999999999999999999L) {
			throw new Exception("金额过大");
		}
		long integral = money / 100;// 整数部分
		int integralLen = String.valueOf(integral).length();
		int decimal = (int) (money % 100);// 小数部分
		if (integral > 0) {
			int zeroCount = 0;
			for (int i = 0; i < integralLen; i++) {
				int unitLen = integralLen - i - 1;
				// 当前数字的值
				int d = Integer.parseInt(String.valueOf(integral).substring(i, i + 1));
				// 大单位的下标{"", "万", "亿"}
				int quotient = unitLen / 4;
				// 获取单位的下标（整数部分都是以4个数字一个大单位，比如：个、十、百、千、个万、十万、百万、千万、个亿、十亿、百亿、千亿）
				int modulus = unitLen % 4;
				if (d == 0) {
					zeroCount++;
				} else {
					if (zeroCount > 0) {
						result.append(CN_UPPER_NUMBER[0]);
					}
					zeroCount = 0;
					result.append(CN_UPPER_NUMBER[d]).append(RADICES[modulus]);
				}
				if (modulus == 0 && zeroCount < 4) {
					result.append(BIG_RADICES[quotient]);
				}
			}
			result.append("元");
		}
		if (decimal > 0) {
			int j = decimal / 10;
			if (j > 0) {
				result.append(CN_UPPER_NUMBER[j]).append("角");
			}
			j = decimal % 10;
			if (j > 0) {
				result.append(CN_UPPER_NUMBER[j]).append("分");
			}
		} else {
			result.append("整");
		}
		return result.toString();
	}

	/**
	 * 行复制功能
	 *
	 * @param fromRow
	 * @param toRow
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void copyRow(Workbook wb, Row fromRow, Row toRow, boolean copyValueFlag) throws Exception {
		// toRow.setHeight(fromRow.getHeight());
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			Cell tmpCell = (Cell) cellIt.next();
			Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
			CopyCell(wb, tmpCell, newCell, copyValueFlag);
		}
		Sheet worksheet = fromRow.getSheet();
		int total = worksheet.getNumMergedRegions();
		for (int i = 0; i < total; i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == fromRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(toRow.getRowNum(),
						(toRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegionUnsafe(newCellRangeAddress);
			}
		}
	}

	public static void copyRowRebulidMerged(Workbook wb, Row fromRow, Row toRow, boolean copyValueFlag)
			throws Exception {
		// toRow.setHeight(fromRow.getHeight());

		// 拆分单元格
		Sheet sheet = toRow.getSheet();
		/*for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--) {
		}*/
		sheet.removeMergedRegion(0);
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			Cell tmpCell = (Cell) cellIt.next();
			Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
			CopyCell(wb, tmpCell, newCell, copyValueFlag);
		}
		Sheet worksheet = fromRow.getSheet();
		int total = worksheet.getNumMergedRegions();
		for (int i = 0; i < total; i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == fromRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(toRow.getRowNum(),
						(toRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegionUnsafe(newCellRangeAddress);
			}
		}
	//	HSSFSheet sheet1 = (HSSFSheet) toRow.getSheet();
		// 合并单元格
	//	sheet.addMergedRegion(new CellRangeAddress(toRow.getRowNum(), toRow.getRowNum(), 3, 5));

	}

	/**
	 * 行复制功能+hight
	 *
	 * @param fromRow
	 * @param toRow
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void copyRowHight(Workbook wb, Row fromRow, Row toRow, boolean copyValueFlag) throws Exception {
		toRow.setHeight(fromRow.getHeight());
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			Cell tmpCell = (Cell) cellIt.next();
			Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
			CopyCell(wb, tmpCell, newCell, copyValueFlag);
		}
		Sheet worksheet = fromRow.getSheet();
		int total = worksheet.getNumMergedRegions();
		for (int i = 0; i < total; i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == fromRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(toRow.getRowNum(),
						(toRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegionUnsafe(newCellRangeAddress);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void copyRowHightWithoutValue(Workbook wb, Row fromRow, Row toRow, boolean copyValueFlag)
			throws Exception {
		toRow.setHeight(fromRow.getHeight());
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			Cell tmpCell = (Cell) cellIt.next();
			Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
			CopyCellWithoutValue(wb, tmpCell, newCell, copyValueFlag);
		}
		Sheet worksheet = fromRow.getSheet();
		int total = worksheet.getNumMergedRegions();
		for (int i = 0; i < total; i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == fromRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(toRow.getRowNum(),
						(toRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegionUnsafe(newCellRangeAddress);
			}
		}
	}

	/**
	 * 复制单元格
	 *
	 * @param srcCell
	 * @param distCell
	 * @param copyValueFlag
	 *            true则连同cell的内容一起复制
	 */
	public static void copyCell(Workbook wb, Cell srcCell, Cell distCell, boolean copyValueFlag) {
		CellStyle newStyle = wb.createCellStyle();
		CellStyle srcStyle = srcCell.getCellStyle();
		newStyle.cloneStyleFrom(srcStyle);
		newStyle.setFont(wb.getFontAt(srcStyle.getFontIndex()));

		// 样式
		distCell.setCellStyle(newStyle);
		// 评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		CellType srcCellType = srcCell.getCellTypeEnum();
		distCell.setCellType(srcCellType);
		if (copyValueFlag) {
			if (srcCellType == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(srcCell)) {
					distCell.setCellValue(srcCell.getDateCellValue());
				} else {
					distCell.setCellValue(srcCell.getNumericCellValue());
				}
			} else if (srcCellType == CellType.STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == CellType.BLANK) {

			} else if (srcCellType == CellType.BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == CellType.ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == CellType.FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else {
			}
		}
	}

	/**
	 * @Description: TODO(超过4000时使用的)</br>
	 * @Title: CopyCell </br>
	 * @param @param
	 *            wb
	 * @param @param
	 *            srcCell
	 * @param @param
	 *            distCell
	 * @param @param
	 *            copyValueFlag
	 * @param @throws
	 *            Exception 设定文件</br>
	 * @return void 返回类型</br>
	 * @throws</br>
	 * @author wyang
	 */
	public static void CopyCell(Workbook wb, Cell srcCell, Cell distCell, boolean copyValueFlag) throws Exception {
		// ICellStyle newstyle = toWb.CreateCellStyle();
		CellStyle newStyle = CreateCellStyle(wb, srcCell.getCellStyle());
		CellStyle srcStyle = srcCell.getCellStyle();
		newStyle.cloneStyleFrom(srcStyle);
		newStyle.setFont(wb.getFontAt(srcStyle.getFontIndex()));
		// 样式
		distCell.setCellStyle(newStyle);
		// 评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		CellType srcCellType = srcCell.getCellTypeEnum();
		distCell.setCellType(srcCellType);
		if (copyValueFlag) {
			if (srcCellType == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(srcCell)) {
					distCell.setCellValue(srcCell.getDateCellValue());
				} else {
					distCell.setCellValue(srcCell.getNumericCellValue());
				}
			} else if (srcCellType == CellType.STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == CellType.BLANK) {

			} else if (srcCellType == CellType.BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == CellType.ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == CellType.FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else {
			}
		}
	}

	public static void CopyCellWithoutValue(Workbook wb, Cell srcCell, Cell distCell, boolean copyValueFlag)
			throws Exception {
		// ICellStyle newstyle = toWb.CreateCellStyle();
		CellStyle newStyle = CreateCellStyle(wb, srcCell.getCellStyle());
		CellStyle srcStyle = srcCell.getCellStyle();
		newStyle.cloneStyleFrom(srcStyle);
		newStyle.setFont(wb.getFontAt(srcStyle.getFontIndex()));
		// 样式
		distCell.setCellStyle(newStyle);
		// 评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		CellType srcCellType = srcCell.getCellTypeEnum();
		distCell.setCellType(srcCellType);
	}

	public static CellStyle CreateCellStyle(Workbook wb, CellStyle fromStyle) throws Exception {
		CellStyle newStyle = getCellStyleCache(fromStyle);
		if (newStyle == null) {
			newStyle = wb.createCellStyle();
		}
		// ICellStyle newStyle = wb.CreateCellStyle();
		return newStyle;
	}

	/**
	 * @Title: getCellStyleCache </br>
	 * @Description: TODO(样式缓存)</br>
	 * @param @param
	 *            fromStyle
	 * @param @return
	 * @param @throws
	 *            Exception 设定文件</br>
	 * @return CellStyle 返回类型</br>
	 * @throws</br>
	 */
	private static CellStyle getCellStyleCache(final CellStyle fromStyle) throws Exception {
		Cache<Integer, CellStyle> cache = CacheBuilder.newBuilder().maximumSize(10)// 设置大小，条目数
				.expireAfterWrite(20, TimeUnit.SECONDS)// 设置失效时间，创建时间
				.expireAfterAccess(20, TimeUnit.HOURS) // 设置时效时间，最后一次被访问
				.removalListener(new RemovalListener<Object, Object>() { // 移除缓存的监听器
					public void onRemoval(RemovalNotification<Object, Object> notification) {
						System.out.println("有缓存数据被移除了");
					}
				}).build(new CacheLoader<Integer, CellStyle>() { // 通过回调加载缓存
					@Override
					public CellStyle load(Integer name) throws Exception {
						CellStyle newStyle = fromStyle;
						return newStyle;
					}
				});
		// cache.invalidateAll();
		return fromStyle;
	}

}

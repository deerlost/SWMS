package com.inossem.core;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * 报表复制生成打印基类
 * 
 * @author 高海涛
 */
public class PrintBaseCopy {

	/**
	 * 范围样式copy
	 * 
	 * @param formSheet
	 *            旧sheet名
	 * @param toSheet
	 *            目标sheet名
	 * @param pageStartRow
	 *            开始行
	 * @param pageEndRow
	 *            结束行
	 * @param startCol
	 *            开始行
	 * @param endCol
	 *            结束列
	 * @param readPlace
	 *            位置
	 * @param hssInputExcelFile
	 *            模板
	 */
	@SuppressWarnings("deprecation")
	public void mdRangeCopy(String formSheet, String toSheet, int pageStartRow, int pageEndRow, int startCol,
			int endCol, int readPlace, HSSFWorkbook hssInputExcelFile) {
		// 源行
		HSSFRow oldRow = null;
		// 目标行
		HSSFRow targetRow = null;
		// 源单元格
		HSSFCell oldCell = null;
		// 目标单元格
		HSSFCell targetCell = null;
		// 源sheet
		HSSFSheet oldSheet = null;
		// 目标Sheet
		HSSFSheet targetSheet = null;
		// 格式
		int lcType;
		// 行
		int lcRow;

		if ((pageStartRow == -1) || (pageEndRow == -1)) {
			return;
		}
		oldSheet = hssInputExcelFile.getSheet(formSheet);
		targetSheet = hssInputExcelFile.getSheet(toSheet);
		int num = oldSheet.getNumMergedRegions();

		// 复制结合单元格
		CellRangeAddress region = null;
		for (lcRow = 0; lcRow < num; lcRow++) {
			region = oldSheet.getMergedRegion(lcRow);
			int targetRowFrom = region.getFirstRow() + readPlace;
			int targetRowTo = region.getLastRow() + readPlace;
			CellRangeAddress newRegion = region.copy();
			newRegion.setFirstRow(targetRowFrom);
			newRegion.setFirstColumn(region.getFirstColumn());
			newRegion.setLastRow(targetRowTo);
			newRegion.setLastColumn(region.getLastColumn());
			targetSheet.addMergedRegion(newRegion);
		}

		// 列幅设置
		if (pageStartRow == 0) {
			for (lcRow = pageStartRow; lcRow <= pageEndRow; lcRow++) {
				oldRow = oldSheet.getRow(lcRow);
				if (oldRow != null) {
					for (short i = (short) startCol; i <= endCol; i++) {
						targetSheet.setColumnWidth(i, oldSheet.getColumnWidth(i));
					}
					break;
				}
			}
		}

		// 行copy
		for (lcRow = pageStartRow; lcRow <= pageEndRow; lcRow++) {
			oldRow = oldSheet.getRow(lcRow);
			if (oldRow == null) {
				continue;
			}

			targetRow = targetSheet.getRow(lcRow - pageStartRow + readPlace);
			if (targetRow == null) {
				targetRow = targetSheet.createRow(lcRow - pageStartRow + readPlace);
			}
			targetRow.setHeight(oldRow.getHeight());
			for (short i = (short) (startCol); i <= endCol; i++) {
				oldCell = oldRow.getCell(i);
				if (oldCell == null) {
					continue;
				}
				targetCell = targetRow.createCell(i);
				targetCell.setCellStyle(oldCell.getCellStyle());
				lcType = oldCell.getCellType();
				targetCell.setCellType(lcType);
				switch (lcType) {
				case HSSFCell.CELL_TYPE_BOOLEAN:
					targetCell.setCellValue(oldCell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					targetCell.setCellErrorValue(oldCell.getErrorCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					targetCell.setCellFormula(this.parseFormula(oldCell.getCellFormula()));
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					targetCell.setCellValue(oldCell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_STRING:
					targetCell.setCellValue(oldCell.getStringCellValue());
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					targetCell.setCellValue(oldCell.getStringCellValue());
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * Formula
	 * 
	 * @param pPOIFormula
	 * @return Formula
	 */
	private String parseFormula(String pPOIFormula) {
		final String cstReplaceString = "ATTR(semiVolatile)";
		StringBuffer result = null;
		int index;

		result = new StringBuffer();
		index = pPOIFormula.indexOf(cstReplaceString);
		if (index >= 0) {
			result.append(pPOIFormula.substring(0, index));
			result.append(pPOIFormula.substring(index + cstReplaceString.length()));
		} else {
			result.append(pPOIFormula);
		}

		return result.toString();
	}

	/**
	 * 打印设置
	 * 
	 * @date 2017年9月18日
	 * @author 高海涛
	 * @param hssInputExcelSheet
	 * @param hssOutputExcelSheet
	 */
	public void printSet(HSSFSheet hssInputExcelSheet, HSSFSheet hssOutputExcelSheet) {
		try {
			// sheet set
			HSSFPrintSetup hssFromSheetSet = hssInputExcelSheet.getPrintSetup();
			hssOutputExcelSheet.setSelected(true);
			hssOutputExcelSheet.getPrintSetup().setPaperSize(hssFromSheetSet.getPaperSize());
			hssOutputExcelSheet.getPrintSetup().setLandscape(hssFromSheetSet.getLandscape());
			hssOutputExcelSheet.getPrintSetup().setHeaderMargin(hssFromSheetSet.getHeaderMargin());
			hssOutputExcelSheet.getPrintSetup().setFooterMargin(hssFromSheetSet.getFooterMargin());
			hssOutputExcelSheet.getPrintSetup().setScale((short) 50);
			hssOutputExcelSheet.setHorizontallyCenter(hssInputExcelSheet.getHorizontallyCenter());
			hssOutputExcelSheet.setVerticallyCenter(true);

			hssOutputExcelSheet.setAutobreaks(true);
			hssOutputExcelSheet.setHorizontallyCenter(true); // 设置打印页面为水平居中

			// sheet 打印比例.（3/4）=0.75 = 75%
			hssOutputExcelSheet.setZoom(1, 1);
			// set display grid lines or not
			hssOutputExcelSheet.setDisplayGridlines(true);
			// set print grid lines or not
			hssOutputExcelSheet.setPrintGridlines(true);
			hssOutputExcelSheet.setMargin(HSSFSheet.TopMargin, 0);
			hssOutputExcelSheet.setMargin(HSSFSheet.BottomMargin, 0);
			hssOutputExcelSheet.setMargin(HSSFSheet.LeftMargin, 0);
			hssOutputExcelSheet.setMargin(HSSFSheet.RightMargin, 0);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 标签打印设置
	 * 
	 * @date 2017年9月18日
	 * @author 高海涛
	 * @param hssInputExcelSheet
	 * @param hssOutputExcelSheet
	 */
	public void printSetLabel(HSSFSheet hssInputExcelSheet, HSSFSheet hssOutputExcelSheet) {
		try {
			// sheet set
			HSSFPrintSetup hssFromSheetSet = hssInputExcelSheet.getPrintSetup();
			// hssOutputExcelSheet.setSelected(true);
			hssOutputExcelSheet.setAutobreaks(false);
			hssOutputExcelSheet.setHorizontallyCenter(true); // 设置打印页面为水平居中
			// sheet 打印比例.（3/4）=0.75 = 75%
			hssOutputExcelSheet.setZoom(1, 1);
			// set display grid lines or not
			hssOutputExcelSheet.setDisplayGridlines(true);
			// set print grid lines or not
			hssOutputExcelSheet.setPrintGridlines(false);
			hssOutputExcelSheet.getPrintSetup().setScale((short) 75);

			hssOutputExcelSheet.getPrintSetup().setPaperSize(hssFromSheetSet.getPaperSize());
			hssOutputExcelSheet.getPrintSetup().setLandscape(hssFromSheetSet.getLandscape());
			hssOutputExcelSheet.getPrintSetup().setHeaderMargin(hssFromSheetSet.getHeaderMargin());
			hssOutputExcelSheet.getPrintSetup().setFooterMargin(hssFromSheetSet.getFooterMargin());
			hssOutputExcelSheet.setMargin(HSSFSheet.TopMargin, 0);
			hssOutputExcelSheet.setMargin(HSSFSheet.BottomMargin, 0);
			hssOutputExcelSheet.setMargin(HSSFSheet.LeftMargin, 0);
			hssOutputExcelSheet.setMargin(HSSFSheet.RightMargin, 0);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 获取生成路径
	 * 
	 * @date 2017年9月18日
	 * @author 高海涛
	 * @return
	 */
	public String getOutPutFilePath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String directory = request.getServletContext().getRealPath(Constant.OUTPUTFILE_LABEL_PATH);
		this.createWorkbookDirectory(Constant.OUTPUTFILE_LABEL_PATH);
		return directory;
	}

	/**
	* @Title: getOutPutFileTablePath </br>
	* @Description: TODO(获取生成路径 及名称)</br>
	* @param @return    设定文件</br>
	* @return String    返回类型</br>
	* @throws</br>
	*/
	public String getOutPutFileTablePath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String directory = request.getServletContext()
				.getRealPath(Constant.OUTPUTFILE_TABLE_PATH + new Date().getTime());
		this.createWorkbookDirectory(Constant.OUTPUTFILE_TABLE_PATH);
		return directory;
	}

	/**
	 * 获取二维码生成路径
	 * 
	 * @date 2017年9月21日
	 * @author 高海涛
	 * @return
	 */
	public String getOutPutQRcodeFilePath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String directory = request.getServletContext()
				.getRealPath(Constant.OUTPUTFILE_QRCODE_PATH + new Date().getTime());
		this.createWorkbookDirectory(Constant.OUTPUTFILE_QRCODE_PATH);
		return directory;
	}

	/**
	 * 通过Excel报表的URL获取到对应PDF报表的URL
	 * 
	 * @param excelUrl
	 * @return
	 * @throws Exception
	 */
	public boolean getPdfRopertUrl(String excelUrl, String pdfPath) throws Exception {

		File inputFile = new File(excelUrl);
		File outputFile = new File(pdfPath);

		// 启动OpenOffice的服务
		// String OpenOffice_HOME = "C:\\Program Files (x86)\\OpenOffice 4\\";
		// String command = OpenOffice_HOME + "program\\soffice.exe -headless
		// -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
		// Process pro = Runtime.getRuntime().exec(command);

		// connect to an OpenOffice.org instance running on port 8100
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		connection.connect();
		// convert
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		converter.convert(inputFile, outputFile);
		// close the connection
		connection.disconnect();
		// 关闭OpenOffice服务的进程
		// pro.destroy();

		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @date 2017年9月18日
	 * @author 高海涛
	 * @param sPath
	 * @return
	 */
	public boolean deletePrintFile(String sPath) {
		// 文件删除
		/*
		 * try{ File file = new File(sPath); // 路径为文件且不为空则进行删除 if (file.isFile()
		 * && file.exists()) { file.delete(); } } catch(Exception e){ return
		 * false; } return true;
		 */

		// 文件夹删除
		File pathFile = new File(sPath);
		if (pathFile.isDirectory()) {
			// 文件夹下当天文件夹路径
			String pathToday = sPath + File.separator;
			// 列出所有文件夹
			File[] fileArray = pathFile.listFiles();
			for (File file : fileArray) {
				if (!StringUtils.equals(file.getPath(), pathToday)) {
					file.delete();
				}
			}
		}
		return true;
	}

	/**
	 * 标签二维码图片设置
	 * 
	 * @date 2017年9月18日
	 * @author 高海涛
	 * @param hssInputExcelFile
	 * @param hssOutputExcelSheet
	 * @param dateSize
	 */
	public void imageSet(HSSFWorkbook hssInputExcelFile, HSSFSheet hssOutputExcelSheet, String filePath, int readPlace,
			int col1, int row1, int col2, int row2) {
		try {
			HSSFPatriarch patriarch = (HSSFPatriarch) hssOutputExcelSheet.createDrawingPatriarch();

			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			BufferedImage bufferImg = ImageIO.read(new File(filePath));
			ImageIO.write(bufferImg, "png", byteArrayOut);
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) col1, row1 + readPlace, (short) col2,
					row2 + readPlace);
			patriarch.createPicture(anchor,
					hssInputExcelFile.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param workbook
	 * @throws IOException
	 */
	public void createWorkbookDirectory(String path) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String directory = request.getServletContext().getRealPath("") + path;
		File file = new File(directory);
		if (!file.isDirectory()) {
			try {
				FileUtils.forceMkdir(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

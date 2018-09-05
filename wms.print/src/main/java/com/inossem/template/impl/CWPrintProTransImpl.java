package com.inossem.template.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.inossem.core.Common;
import com.inossem.core.Constant;
import com.inossem.core.PrintBaseCopy;
import com.inossem.core.PrintFileUtil;
import com.inossem.core.QRcodeUtil;
import com.inossem.template.CopyTemplate;

public class CWPrintProTransImpl extends PrintBaseCopy implements CopyTemplate {

	@SuppressWarnings({ "unchecked", "unused", "resource" })
	@Override
	public String mdPrint(List<Map> dataList, String type) throws Exception {

		Map<String, Object> header = (Map<String, Object>) dataList.get(0).get("header");
		List<Map<String, Object>> item = (List<Map<String, Object>>) dataList.get(0).get("list");

		// 获取模板
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String filePath = request.getServletContext().getRealPath(Constant.TEMPLATE_LABEL_PROTRANS);
		// 读取模板
		HSSFWorkbook hssInputExeclFile = new HSSFWorkbook(new FileInputStream(filePath));
		File f = new File(filePath);
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(f));
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		// 读取sheet内容
		HSSFSheet sheet = wb.getSheetAt(0);
		// 自动换行
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);

		String path = this.getOutPutFileTablePath();
		// 输出文件
		String OutXlsxFile = path + Constant.OUTPUTFILE_TYPE_XLS;
		String OutPdfFile = path + Constant.OUTPUTFILE_TYPE_PDF;
		// 二维码路径
		String imgPath = this.getOutPutQRcodeFilePath() + Constant.OUTPUTFILE_TYPE_PNG;

		// 条形码
		// JbarcodeUtil2.encode(("1313323"), 4200, 900, imgPath);
		// 二维码
		QRcodeUtil.zxingCodeCreate(String.valueOf(header.get("stock_transport_code")), 400, 400, imgPath, "png");
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		// 加载图片
		BufferedImage bufferImg = ImageIO.read(new File(imgPath));
		ImageIO.write(bufferImg, "jpg", byteArrayOut);
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		/*
		 * dx1 dy1 起始单元格中的x,y坐标.
		 * 
		 * dx2 dy2 结束单元格中的x,y坐标
		 * 
		 * col1,row1 指定起始的单元格，下标从0开始
		 * 
		 * col2,row2 指定结束的单元格 ，下标从0开始
		 */
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 10, 1, (short) 11, 3);
		// 插入图片
		patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));

		// 在相应的单元格进行赋值--header
		sheet.getRow(4).getCell(1).setCellValue(String.valueOf("发出工厂：" + header.get("fty_name")));
		sheet.getRow(4).getCell(4).setCellValue(String.valueOf("司机：" + header.get("driver")));
		sheet.getRow(4).getCell(10).setCellValue(String.valueOf(header.get("stock_transport_code")));
		sheet.getRow(5).getCell(1).setCellValue(String.valueOf("发出库房：" + header.get("location_name")));
		sheet.getRow(5).getCell(4).setCellValue(String.valueOf("车辆：" + header.get("vehicle")));
		sheet.getRow(5).getCell(10).setCellValue(String.valueOf(header.get("create_time")));
		/*
		 * if(header.get("remark") == ""){ Object o =null;
		 * sheet.getRow(7).getCell(1).setCellValue("备注说明：" + String.valueOf(o));
		 * }else{ sheet.getRow(7).getCell(1).setCellValue("备注说明：" +
		 * String.valueOf(header.get("remark"))); }
		 */

		// item
		int rowNum = 9;
		Integer num = (int) 0;
		Integer n = (int) 0;
		for (int i = 0; i < item.size(); i++) {
			if (rowNum != 9) {
				Common.copyRow(wb, sheet.getRow(9), sheet.createRow(rowNum), true);
			}
			sheet.getRow(9 + i).getCell(1).setCellValue(String.valueOf(i + 1));
			sheet.getRow(9 + i).getCell(2).setCellValue(String.valueOf(item.get(i).get("specs_model")));
			sheet.getRow(9 + i).getCell(3).setCellValue(String.valueOf(item.get(i).get("package_type_name")));
			sheet.getRow(9 + i).getCell(4).setCellValue(String.valueOf(item.get(i).get("num"))); // 件数
			n += (Integer) item.get(i).get("num");
			sheet.getRow(9 + i).getCell(5).setCellValue(String.valueOf(item.get(i).get("transport_qty")));// .重量
			num += (Integer) item.get(i).get("transport_qty");
			sheet.getRow(9 + i).getCell(6).setCellValue(String.valueOf(item.get(i).get("product_batch")));
			sheet.getRow(9 + i).getCell(7).setCellValue(String.valueOf(item.get(i).get("erp_batch")));
			sheet.getRow(9 + i).getCell(8).setCellValue(String.valueOf(item.get(i).get("fty_input")));
			sheet.getRow(9 + i).getCell(9).setCellValue(String.valueOf(item.get(i).get("location_input")));
			sheet.getRow(9 + i).getCell(10).setCellValue(String.valueOf(item.get(i).get("remark")));
			rowNum++;
		}

		// 合计
		Common.copyRow(wb, sheet.getRow(9), sheet.createRow(rowNum), true);
		sheet.getRow(rowNum).getCell(1).setCellValue(String.valueOf("合计"));
		sheet.getRow(rowNum).getCell(2).setCellValue("");
		sheet.getRow(rowNum).getCell(3).setCellValue("");
		sheet.getRow(rowNum).getCell(4).setCellValue(n);
		sheet.getRow(rowNum).getCell(5).setCellValue(num);
		sheet.getRow(rowNum).getCell(6).setCellValue("");
		sheet.getRow(rowNum).getCell(7).setCellValue("");
		sheet.getRow(rowNum).getCell(8).setCellValue("");
		sheet.getRow(rowNum).getCell(9).setCellValue("");
		sheet.getRow(rowNum).getCell(10).setCellValue("");
		/*
		 * 2,3复联
		 * 
		 */
		int lastRowNum = item.size() + 11;
		sheet.setRowBreak(lastRowNum);

		/*
		 * for (int i = 3; i < lastRowNum; i++) { //Common.copyRow(wb,
		 * sheet.getRow(3), , true); sheet.createRow(item.size() +13); //
		 * sheet.getRow(item.size()
		 * +12).getCell(1).setCellValue(String.valueOf("以下为转运单的附本："));
		 * Common.copyRow(wb, sheet.getRow(4), sheet.createRow(lastRowNum + i),
		 * true); }
		 */
		/**
		 * 二维码图片加载
		 *//*
			 * anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 11,
			 * lastRowNum+2, (short) 13, lastRowNum + 5);
			 * patriarch.createPicture(anchor,
			 * wb.addPicture(byteArrayOut.toByteArray(),
			 * HSSFWorkbook.PICTURE_TYPE_JPEG));
			 * sheet.setRowBreak(lastRowNum*2-1);
			 * 
			 * anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 11,
			 * lastRowNum*2+2, (short) 13, lastRowNum*2 + 5);
			 * patriarch.createPicture(anchor,
			 * wb.addPicture(byteArrayOut.toByteArray(),
			 * HSSFWorkbook.PICTURE_TYPE_JPEG));
			 * sheet.setRowBreak(lastRowNum*3-1);
			 */
		// 设置打印区域
		wb.setPrintArea(0, 0, 12, 0, sheet.getLastRowNum());

		// 修改模板内容导出新模板
		FileOutputStream out = new FileOutputStream(OutXlsxFile);
		wb.write(out);
		out.close();

		// 转换pdf
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		connection.connect();
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		File inputFile = new File(OutXlsxFile);
		File outputFile = new File(OutPdfFile);
		converter.convert(inputFile, outputFile);
		connection.disconnect();

		if (null != String.valueOf(header.get("print_name"))
				&& !(String.valueOf(header.get("print_name"))).equals("")) {
			PrintFileUtil.printFileAction(OutXlsxFile, String.valueOf(header.get("print_name")));
		}

		/*
		 * String[] as = OutPdfFile.split(File.separator); String fileName =
		 * as[as.length-1];
		 * 
		 * return fileName;
		 */
		return OutPdfFile.substring(OutPdfFile.lastIndexOf("\\") + 1);
	}

	@Override
	public HSSFWorkbook makeExcel(HSSFWorkbook hssInputExcelFile, List<Map> dataList, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQRPath() {
		// TODO Auto-generated method stub
		return null;
	}

}

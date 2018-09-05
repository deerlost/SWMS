package com.inossem.template.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.inossem.core.Common;
import com.inossem.core.Constant;
import com.inossem.core.PrintBaseCopy;
import com.inossem.core.PrintFileUtil;
import com.inossem.core.PropertiesUtil;
import com.inossem.core.QRcodeUtil;
import com.inossem.template.CopyTemplate;

public class CWPrintNewProTransImpl extends PrintBaseCopy implements CopyTemplate {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String mdPrint(List<Map> dataList, String type) throws Exception {

		Map<String, Object> header = (Map<String, Object>) dataList.get(0).get("header");
		List<Map<String, Object>> item = (List<Map<String, Object>>) dataList.get(0).get("list");

		// 获取模板
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String filePath = request.getServletContext().getRealPath(Constant.TEMPLATE_LABEL_PROTRANS);
		// 读取模板
	//	HSSFWorkbook hssInputExeclFile = new HSSFWorkbook(new FileInputStream(filePath));
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

		// 二维码
		QRcodeUtil.zxingCodeCreate(Constant.ANDROID_TYPE_6+String.valueOf(header.get("stock_transport_code")), 400, 400, imgPath, "png");
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		// 加载图片
		BufferedImage bufferImg = ImageIO.read(new File(imgPath));
		ImageIO.write(bufferImg, "jpg", byteArrayOut);
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

		int count = 0;
		int size = 0;
		if (item.size() % 5 == 0) {
			size = item.size() / 5;
		} else {
			size = item.size() / 5 + 1;
		}
		List<Map<String, Object>> arrayitem = Lists.newArrayList(item);
		Iterable<List<Map<String, Object>>> subList = Iterables.partition(arrayitem, 5);
		Iterator<List<Map<String, Object>>> iterator = subList.iterator();
		while (iterator.hasNext()) {
			// for (int j = 0; j < 2; j++) {
			List<Map<String, Object>> printItem = iterator.next();

			/*
			 * dx1 dy1 起始单元格中的x,y坐标.
			 * 
			 * dx2 dy2 结束单元格中的x,y坐标
			 * 
			 * col1,row1 指定起始的单元格，下标从0开始
			 * 
			 * col2,row2 指定结束的单元格 ，下标从0开始
			 */
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 10, 1 + count * 35, (short) 11,
					3 + count * 35);
			// 插入图片
			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));

			// 在相应的单元格进行赋值--header
			// 根据类型判断上下架

			sheet.getRow(4 + count * 35).getCell(1).setCellValue(String.valueOf("发出工厂：" + header.get("fty_name")));
			sheet.getRow(4 + count * 35).getCell(4).setCellValue(String.valueOf("司机：" + header.get("driver")));
			sheet.getRow(4 + count * 35).getCell(10).setCellValue(String.valueOf(header.get("stock_transport_code")));// 作业单号
			sheet.getRow(5 + count * 35).getCell(1).setCellValue(String.valueOf("发出库房：" + header.get("location_name")));
			sheet.getRow(5 + count * 35).getCell(4).setCellValue(String.valueOf("车辆：" + header.get("vehicle")));
			sheet.getRow(5 + count * 35).getCell(10).setCellValue(String.valueOf(header.get("create_time")));// 创建日期
			sheet.getRow(6 + count * 35).getCell(1).setCellValue(String.valueOf("备注说明：" + header.get("remark")));

			// item
			double num = 0;
			double qty = 0;
			for (int i = 0; i < printItem.size(); i++) {
				sheet.getRow(10 + i + count * 35).getCell(1).setCellValue(String.valueOf(i + 1 + count * 5));
				sheet.getRow(10 + i + count * 35).getCell(2)
						.setCellValue(String.valueOf(item.get(i).get("specs_model")));
				sheet.getRow(10 + i + count * 35).getCell(3)
						.setCellValue(String.valueOf(item.get(i).get("package_type_name")));
				sheet.getRow(10 + i + count * 35).getCell(4).setCellValue(String.valueOf(item.get(i).get("num")));
				num += Double.parseDouble(String.valueOf(item.get(i).get("num")));
				sheet.getRow(10 + i + count * 35).getCell(5)
						.setCellValue(String.valueOf(item.get(i).get("transport_qty")));
				qty += Double.parseDouble(String.valueOf(item.get(i).get("transport_qty")));
				sheet.getRow(10 + i + count * 35).getCell(6)
						.setCellValue(String.valueOf(item.get(i).get("product_batch")));
				sheet.getRow(10 + i + count * 35).getCell(7).setCellValue(String.valueOf(item.get(i).get("erp_batch")));

				sheet.getRow(10 + i + count * 35).getCell(8).setCellValue(String.valueOf(item.get(i).get("fty_input")));
				sheet.getRow(10 + i + count * 35).getCell(9)
						.setCellValue(String.valueOf(item.get(i).get("location_input")));
				sheet.getRow(10 + i + count * 35).getCell(10).setCellValue(String.valueOf(item.get(i).get("remark")));
			}
			sheet.getRow(15 + count * 35).getCell(1).setCellValue(String.valueOf("合计"));
			sheet.getRow(15 + count * 35).getCell(4).setCellValue(num);
			sheet.getRow(15 + count * 35).getCell(5).setCellValue(qty);

			sheet.getRow(17 + count * 35).getCell(2).setCellValue(String.valueOf("创建人：" + header.get("create_user")));//
			sheet.getRow(17 + count * 35).getCell(7).setCellValue(String.valueOf("打印日期：" + header.get("print_date")));// 日期
			// Common.copyRow(wb, sheet.getRow(18), sheet.createRow(19), true);
			// sheet.getRow(19 + count *
			// 35).getCell(1).setCellValue("以下为出库下架单的附本：");
			/*
			 * 2复联
			 * 
			 */
			int lastRowNum = 20 + count * 35;

			for (int i = 0; i < 15; i++) {
				Common.copyRowHight(wb, sheet.getRow(i + 3 + count * 35), sheet.createRow(lastRowNum + i), true);
			}

			if (count < size - 1) {

				for (int i = 0; i < 35; i++) {
					if (19 <= i && i <= 25 || i < 11) {
						Common.copyRow(wb, sheet.getRow(i), sheet.createRow(35 * (count + 1) + i), true);
						sheet.getRow(35 * (count + 1) + i).setHeight(sheet.getRow(i).getHeight());
					} else {
						Common.copyRowHightWithoutValue(wb, sheet.getRow(i), sheet.createRow(35 * (count + 1) + i),
								true);
						sheet.getRow(35 * (count + 1) + i).setHeight(sheet.getRow(i).getHeight());

					}
				}
			}
			sheet.setRowBreak(34 + count * 35);
			count++;
		}

		// 设置打印区域
		wb.setPrintArea(0, 0, 10, 0, sheet.getLastRowNum());

		// 修改模板内容导出新模板
		FileOutputStream out = new FileOutputStream(OutXlsxFile);
		wb.write(out);
		out.close();

		// 转换pdf
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8110);
		connection.connect();
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		File inputFile = new File(OutXlsxFile);
		File outputFile = new File(OutPdfFile);
		converter.convert(inputFile, outputFile);
		connection.disconnect();

		if (null != String.valueOf(header.get("print_name"))
				&& !(String.valueOf(header.get("print_name")).equals(""))) {
			PrintFileUtil.printFileAction(OutXlsxFile, String.valueOf(header.get("print_name")));
		}

		// 截取文件名

		 String[] as = OutPdfFile.split(PropertiesUtil.getInstance().getSeparator());
		 String fileName = as[as.length - 1];
		
		 return fileName;

	//	return OutPdfFile.substring(OutPdfFile.lastIndexOf(File.separator) + 1);

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

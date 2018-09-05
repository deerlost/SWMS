package com.inossem.template.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
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

public class CWPrintOutputImpl extends PrintBaseCopy implements CopyTemplate {

	@SuppressWarnings("unchecked")
	@Override
	public String mdPrint(List<Map> dataList, String type) throws Exception {
		Map<String, Object> header = (Map<String, Object>) dataList.get(0).get("header");
		List<Map<String, Object>> item = (List<Map<String, Object>>) dataList.get(0).get("list");

		// 获取模板
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String filePath = request.getServletContext().getRealPath(Constant.TEMPLATE_LABEL_OUTPUT);
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
		String OutXlsFile = path + Constant.OUTPUTFILE_TYPE_XLS;
		String OutPdfFile = path + Constant.OUTPUTFILE_TYPE_PDF;
		// 二维码路径
		String imgPath = this.getOutPutQRcodeFilePath() + Constant.OUTPUTFILE_TYPE_PNG;

		// 条形码
		// JbarcodeUtil2.encode(("1313323"), 4200, 900, imgPath);
		// 二维码
		QRcodeUtil.zxingCodeCreate(String.valueOf(header.get("allocate_cargo_code")), 400, 400, imgPath, "png");
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
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 10, 3, (short) 12, 5);
		// 插入图片
		patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));

		sheet.getRow(5).getCell(2).setCellValue(String.valueOf(header.get("deliveryVehicle")));
		sheet.getRow(5).getCell(5).setCellValue(String.valueOf(header.get("receiveName")));
		sheet.getRow(5).getCell(9).setCellValue(String.valueOf(header.get("createTime")));

		int rowNum = 8;
		Integer num = (int) 0;
		Integer n = (int) 0;
		Common.copyRowHight(wb, sheet.getRow(10), sheet.createRow( item.size() +9), true);
		for (int i = 0; i <item.size(); i++) {
			if (rowNum != 8) {
				Common.copyRow(wb, sheet.getRow(8), sheet.createRow(rowNum), true);
			}
			sheet.getRow(8 + i).getCell(1).setCellValue(String.valueOf(item.get(i).get("refer_receipt_code")));// 提货单号
			
			String mat =String.valueOf(item.get(i).get("mat_code"))+item.get(i).get("mat_name");
			int rowHeightRatio = new String(mat.getBytes("gb2312"),"iso-8859-1").length()/24 + 1;
			if(rowHeightRatio > 1){
				HSSFRow row = sheet.getRow(8 + i);
				row.setHeight((short) (row.getHeight()*rowHeightRatio));
			}
			sheet.getRow(8 + i).getCell(3).setCellValue(mat);// 品名规格
			sheet.getRow(8 + i).getCell(5).setCellValue(String.valueOf(item.get(i).get("product_batch")));// 生产批次
			sheet.getRow(8 + i).getCell(6).setCellValue(String.valueOf(item.get(i).get("qty")));// 数量
			num += (Integer) item.get(i).get("qty");
			sheet.getRow(8 + i).getCell(7).setCellValue(String.valueOf(item.get(i).get("num")));// 件数
			n += (Integer) item.get(i).get("num");
			sheet.getRow(8 + i).getCell(8).setCellValue(String.valueOf(item.get(i).get("area_name")));// 存储区
			sheet.getRow(8 + i).getCell(9).setCellValue(String.valueOf(item.get(i).get("position_code")));// 仓位
			String remark ="";
			if(null != item.get(i).get("remark") && !item.get(i).get("remark").equals("")){
				remark = String.valueOf(item.get(i).get("remark"));
			}
			sheet.getRow(8 + i).getCell(10).setCellValue(remark);// 备注
			rowNum++;
			
		}

		Common.copyRow(wb, sheet.getRow(9), sheet.createRow(rowNum), true);
		sheet.getRow(rowNum).getCell(1).setCellValue(" ");
		sheet.getRow(rowNum).getCell(3).setCellValue(" ");
		sheet.getRow(rowNum).getCell(5).setCellValue(" ");
		sheet.getRow(rowNum).getCell(6).setCellValue("合计" + " " + String.valueOf(num) + "T");
		sheet.getRow(rowNum).getCell(7).setCellValue("合计" + " " + String.valueOf(n) + "件");
		sheet.getRow(rowNum).getCell(8).setCellValue(" ");
		sheet.getRow(rowNum).getCell(9).setCellValue(" ");
		sheet.getRow(rowNum).getCell(10).setCellValue(" ");

	
		int lastRowNum = item.size() + 10;
		sheet.setRowBreak(lastRowNum);
/*
		for (int i = 0; i < lastRowNum; i++) {
			if (i == 3) {
				Common.copyRowHight(wb, sheet.getRow(3), sheet.createRow(lastRowNum + i), true);
				Common.copyRowHight(wb, sheet.getRow(3), sheet.createRow(lastRowNum * 2 + i), true);
			} else if (i == lastRowNum - 1) {
				Common.copyRowHight(wb, sheet.getRow(lastRowNum - 1), sheet.createRow(lastRowNum + i), true);
				Common.copyRowHight(wb, sheet.getRow(lastRowNum - 1), sheet.createRow(lastRowNum * 2 + i), true);
			} else {
				Common.copyRowHight(wb, sheet.getRow(i), sheet.createRow(lastRowNum + i), true);
				Common.copyRowHight(wb, sheet.getRow(i), sheet.createRow(lastRowNum * 2 + i), true);
			}
		}

	
		
		anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 10, lastRowNum + 3, (short) 12, lastRowNum + 5);
		patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
		sheet.setRowBreak(lastRowNum*2-1);
		
		
		anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 10, lastRowNum * 2 + 3, (short) 12, lastRowNum * 2 + 5);
		patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
		sheet.setRowBreak(lastRowNum*3-1);*/

		wb.setPrintArea(0, 0, 12, 0, sheet.getLastRowNum());

		FileOutputStream out = new FileOutputStream(OutXlsFile);
		wb.write(out);
		out.close();

		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		connection.connect();
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		File inputFile = new File(OutXlsFile);
		File outputFile = new File(OutPdfFile);
		converter.convert(inputFile, outputFile);
		connection.disconnect();

		if(Integer.parseInt(String.valueOf(header.get("isPDA")))==1){
			PrintFileUtil.printFileAction(OutXlsFile,String.valueOf(header.get("printPath")));
		}
//		String[] as = OutPdfFile.split(File.separator);
//		String fileName = as[as.length-1];
//		
//		return  fileName;
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

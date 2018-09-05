package com.inossem.template.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
import com.inossem.core.PropertiesUtil;
import com.inossem.template.CopyTemplate;

public class CWPrintErpStockImpl extends PrintBaseCopy implements CopyTemplate {

	
	@SuppressWarnings("unchecked")
	@Override
	public String mdPrint(List<Map> dataList, String type) throws Exception {

		Map<String, Object> header = (Map<String, Object>) dataList.get(0).get("header");
		List<Map<String, Object>> item = (List<Map<String, Object>>) dataList.get(0).get("list");

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String filePath = request.getServletContext().getRealPath(Constant.TEMPLATE_LABEL_ERPSTOCK);

		HSSFWorkbook hssfInputExecl = new HSSFWorkbook(new FileInputStream(filePath));
		File f = new File(filePath);
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(f));
		HSSFWorkbook wb = new HSSFWorkbook(fs);

		HSSFSheet sheet = wb.getSheetAt(0);

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		String path = this.getOutPutFileTablePath();
		String OutXlsFile = path + Constant.OUTPUTFILE_TYPE_XLS;
		String OutPdfFile = path + Constant.OUTPUTFILE_TYPE_PDF;
		/*
		 * String imgPath =
		 * this.getOutPutQRcodeFilePath()+Constant.OUTPUTFILE_TYPE_PNG;
		 * 
		 * JbarcodeUtil2.encode("1451234512", 4200, 900, imgPath);
		 * ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		 * BufferedImage bufferImg = ImageIO.read(new File(imgPath));
		 * HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		 * HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,0,)
		 */
		sheet.getRow(1).getCell(3).setCellValue(String.valueOf(header.get("fty_code"))+"-"+header.get("fty_name"));
		sheet.getRow(1).getCell(10).setCellValue(String.valueOf(header.get("user_name")));
		sheet.getRow(2).getCell(10).setCellValue(String.valueOf(header.get("print_date")));
		
		int rowNum=6;
		
		for(int i =0;i<item.size();i++){
			if(rowNum!=6){
				Common.copyRow(wb, sheet.getRow(6), sheet.createRow(rowNum), true);
			}
			sheet.getRow(6+i).getCell(1).setCellValue(String.valueOf(i+1));
			sheet.getRow(6+i).getCell(2).setCellValue(String.valueOf(item.get(i).get("mat_code")));
			sheet.getRow(6+i).getCell(3).setCellValue(String.valueOf(item.get(i).get("mat_name")));
			sheet.getRow(6+i).getCell(4).setCellValue(String.valueOf(item.get(i).get("erp_batch")));
			sheet.getRow(6+i).getCell(5).setCellValue(String.valueOf(item.get(i).get("location_code")));
			sheet.getRow(6+i).getCell(6).setCellValue(String.valueOf(item.get(i).get("location_name")));
			sheet.getRow(2).getCell(3).setCellValue(String.valueOf(item.get(i).get("location_code"))+"-"+item.get(i).get("location_name"));
			sheet.getRow(6+i).getCell(7).setCellValue(String.valueOf(item.get(i).get("sap_qty")));
			sheet.getRow(6+i).getCell(8).setCellValue(String.valueOf(item.get(i).get("unit_name")));
			sheet.getRow(6+i).getCell(9).setCellValue(String.valueOf(item.get(i).get("price")));
			sheet.getRow(6+i).getCell(10).setCellValue(String.valueOf(item.get(i).get("wms_qty")));
			sheet.getRow(6+i).getCell(11).setCellValue(String.valueOf(item.get(i).get("num")));
			rowNum++;
		}
		
		int lastRowNum = item.size()+6;
		sheet.setRowBreak(lastRowNum);
		/*for(int i=0;i<lastRowNum;i++){
			if(i==0){
				Common.copyRowHight(wb, sheet.getRow(0), sheet.createRow(lastRowNum+i), true);
				Common.copyRowHight(wb, sheet.getRow(0), sheet.createRow(lastRowNum*2+i), true);
			}else{
				
				Common.copyRow(wb, sheet.getRow(i), sheet.createRow(lastRowNum+i), true);
				Common.copyRow(wb, sheet.getRow(i), sheet.createRow(lastRowNum*2+i), true);
			}
		}*/
		
		wb.setPrintArea(0, 0, 12, 0, sheet.getLastRowNum());
		
		FileOutputStream out = new FileOutputStream(OutXlsFile);
		wb.write(out);
		out.close();
		
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8110);
		connection.connect();
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		File inputFile = new File(OutXlsFile);
		File outputFile = new File(OutPdfFile);
		converter.convert(inputFile, outputFile);
		connection.disconnect();
	
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

package com.inossem.template.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import com.inossem.core.PropertiesUtil;
import com.inossem.template.CopyTemplate;

public class CWPrintLimsImpl extends PrintBaseCopy implements CopyTemplate {

	@SuppressWarnings("unchecked")
	@Override
	public String mdPrint(List<Map> dataList, String type) throws Exception {
		Map<String, Object> header =  (Map<String, Object>) dataList.get(0).get("header");
		List<Map<String, Object>>list = (List<Map<String, Object>>) dataList.get(0).get("list");
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String filePath = request.getServletContext().getRealPath(Constant.TEMPLATE_LABEL_LIMS);
		
		File f = new File(filePath);
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(f));
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		
		HSSFSheet sheet = wb.getSheetAt(0);
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		String path = this.getOutPutFileTablePath();
		String OutXlsFile = path + Constant.OUTPUTFILE_TYPE_XLS;
		String OutPdfFile = path + Constant.OUTPUTFILE_TYPE_PDF;
		
		sheet.getRow(1).getCell(0).setCellValue(String.valueOf(header.get("sampName"))+"质量证明书");
		sheet.getRow(2).getCell(6).setCellValue("取样编号: "+String.valueOf(list.get(0).get("ID_NUMERIC")));
		sheet.getRow(3).getCell(2).setCellValue(String.valueOf(header.get("specification"))); //执行标准
		sheet.getRow(3).getCell(6).setCellValue(String.valueOf(list.get(0).get("SAMPLED_DATE_C")));//日期
		sheet.getRow(4).getCell(2).setCellValue(String.valueOf(header.get("batchName")));//生产批次
		sheet.getRow(4).getCell(6).setCellValue(String.valueOf(list.get(0).get("SAMPLED_DATE_C")));
		sheet.getRow(5).getCell(2).setCellValue(String.valueOf(list.get(0).get("U_ZXBZ")));//执行标准
		sheet.getRow(5).getCell(6).setCellValue(String.valueOf(list.get(0).get("U_SHULIANG")));//受检数量
		
		Common.copyRowHight(wb, sheet.getRow(13), sheet.createRow(7+list.size()+5), true);
		Common.copyRowHight(wb, sheet.getRow(12), sheet.createRow(7+list.size()+4), true);
		Common.copyRowHight(wb, sheet.getRow(11), sheet.createRow(7+list.size()+3), true);
		Common.copyRowHight(wb, sheet.getRow(10), sheet.createRow(7+list.size()+2), true);
		Common.copyRowHight(wb, sheet.getRow(9), sheet.createRow(7+list.size()+1), true);
		Common.copyRowHight(wb, sheet.getRow(8), sheet.createRow(7+list.size()), true);
		int rowNum =7; 
		for(int i =0;i<list.size();i++){
			if (rowNum != 7 && rowNum != 11) {
				Common.copyRow(wb, sheet.getRow(7), sheet.createRow(rowNum), true);
			}else if(rowNum == 11){
				Common.copyRowRebulidMerged(wb, sheet.getRow(7), sheet.createRow(rowNum), true);
			}
			sheet.getRow(7+i).getCell(1).setCellValue(String.valueOf(list.get(i).get("R_NAME")));
			sheet.getRow(7+i).getCell(2).setCellValue(String.valueOf(list.get(i).get("R_UNITS")));
			sheet.getRow(7+i).getCell(3).setCellValue(String.valueOf(list.get(i).get("MV_RANGE")));
			sheet.getRow(7+i).getCell(6).setCellValue(String.valueOf(list.get(i).get("R_TEXT")));
			rowNum++;
		}
		
	
		
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 插入图片

		
		ByteArrayOutputStream byteArrayOutHN = new ByteArrayOutputStream();
		String imgPathHN = "";
		if(String.valueOf(list.get(0).get("S_ON_SPEC_DESC")).equals("优等品")){
			imgPathHN=request.getServletContext().getRealPath(Constant.IMG_EXCELLENT);
		}else if(String.valueOf(list.get(0).get("S_ON_SPEC_DESC")).equals("一等品")){
			imgPathHN=request.getServletContext().getRealPath(Constant.IMG_FIRST);
		}else{
			imgPathHN=request.getServletContext().getRealPath(Constant.IMG_QUALIFIED);
		}
		BufferedImage bufferImgHN = ImageIO.read(new File(imgPathHN));
		ImageIO.write(bufferImgHN, "png", byteArrayOutHN);
		patriarch = sheet.createDrawingPatriarch();
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 8+list.size(), (short) 5,9+list.size());
		patriarch.createPicture(anchor, wb.addPicture(byteArrayOutHN.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
		
		sheet.getRow(10+list.size()).getCell(2).setCellValue(String.valueOf(list.get(0).get("U_MEMO")));
	
		ByteArrayOutputStream byteArrayOutZ = new ByteArrayOutputStream();
		String imgPathZ = request.getServletContext().getRealPath(Constant.IMG_ZHOU);
		BufferedImage bufferImgZ = ImageIO.read(new File(imgPathZ));
		ImageIO.write(bufferImgZ, "png", byteArrayOutZ);
		patriarch = sheet.createDrawingPatriarch();
		HSSFClientAnchor anchorZ = new HSSFClientAnchor(0, 0, 0, 0, (short) 3, 12+list.size(), (short) 4,13+list.size());
		patriarch.createPicture(anchorZ, wb.addPicture(byteArrayOutZ.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
		
		ByteArrayOutputStream byteArrayOutQ = new ByteArrayOutputStream();
		String imgPathQ = request.getServletContext().getRealPath(Constant.IMG_QIU);
		BufferedImage bufferImgQ = ImageIO.read(new File(imgPathQ));
		ImageIO.write(bufferImgQ, "png", byteArrayOutQ);
		patriarch = sheet.createDrawingPatriarch();
		HSSFClientAnchor anchorQ = new HSSFClientAnchor(0, 0, 0, 0, (short) 6, 12+list.size(), (short) 7,13+list.size());
		patriarch.createPicture(anchorQ, wb.addPicture(byteArrayOutQ.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
		
		
		int lastRowNum = 13+list.size();
		sheet.setRowBreak(lastRowNum);
		wb.setPrintArea(0, 0, 7, 0, sheet.getLastRowNum());
		
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
//		return OutPdfFile.substring(OutPdfFile.lastIndexOf(File.separator) +
//				 1);
		
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

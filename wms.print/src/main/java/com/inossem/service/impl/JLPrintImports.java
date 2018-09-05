package com.inossem.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.inossem.core.Constant;
import com.inossem.core.PrintBaseCreate;
import com.inossem.core.QRcodeUtil;
import com.inossem.service.PrintService;

/** 
* @author 高海涛 
* @version 2017年9月27日
* JL到货通知单
*/
public class JLPrintImports extends PrintBaseCreate implements PrintService {

	/**
	 * 生成打印文件
	 */
	@SuppressWarnings("unchecked")
	public String mdPrint(@SuppressWarnings("rawtypes") List<Map> dataList,String type){
		String url = "";
		
		List<Map<String,Object>> list = (List<Map<String,Object>>) dataList.get(0).get("list");
		
		List<Map<String,Object>> header = new ArrayList<Map<String,Object>>();
		Map<String,Object> map =  (Map<String,Object>) dataList.get(0).get("header");
		header.add(map);
		
		try {
			url = getExcelReportUrl("送货通知单",
					list,getDataMappingList(),getOrderList(),
					header,getHeaderList(),getHeaderOrderList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	

	/**
	 * 实现头信息方法
	 */
	@SuppressWarnings("deprecation")
	public int createHeader(Workbook workbook, List<Map<String, Object>> list, List<String> orderList){
		Sheet sheet = workbook.getSheetAt(0);
		
		//第一行
		Row row = sheet.createRow(0);
		row.setHeight((short)570);
		CellStyle hStyle =this.getHeaderCellStyle(workbook);
		Cell c = row.createCell(0);
    	c.setCellValue(new HSSFRichTextString("中国石化股份有限公司-金陵分公司"));
    	c.setCellStyle(hStyle);
    	CellRangeAddress hCra=new CellRangeAddress(0, 0, 0, 9);
    	sheet.addMergedRegion(hCra);
    	
    	//第二行
		row =sheet.createRow(1);
    	row.setHeight((short)570);
    	c = row.createCell(0);
    	c.setCellValue(new HSSFRichTextString("送货通知单"));
    	c.setCellStyle(hStyle);
    	hCra=new CellRangeAddress(1, 1, 0, 9);
    	sheet.addMergedRegion(hCra);
    	
    	//二维码
    	hCra=new CellRangeAddress(0, 1, 10, 10);
    	sheet.addMergedRegion(hCra);
    	
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    	String imagePath = request.getServletContext().getRealPath("") +
    			Constant.OUTPUTFILE_TABLE_PATH + 
    			new SimpleDateFormat("yyyyMMdd").format(new Date()) + 
    			File.separator +
    			workbook.getNameAt(0).getNameName() +
    			Constant.OUTPUTFILE_TYPE_PNG;
    			
    	Map<String,Object> header = list.get(0);
    	String[] titleArray = getTitleArray(list, orderList);
    	String content =header.get("二维码").toString();
		QRcodeUtil.zxingCodeCreate(content,400,400,imagePath,"png");
		
		HSSFPatriarch patriarch = (HSSFPatriarch) workbook.getSheetAt(0).createDrawingPatriarch();
		
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		BufferedImage bufferImg;
		try {
			bufferImg = ImageIO.read(new File(imagePath));
			ImageIO.write(bufferImg, "png", byteArrayOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1, 1, (short) 10, 0, (short) 11, 2);
		patriarch.createPicture(anchor, workbook.addPicture( byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
		
		
		//头信息
		CellStyle dStyle = workbook.createCellStyle();
		dStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		dStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		int cellIndex = 10;	//大于9为新增行
		int rowIndex = 1;
		Cell cell =null;
		Row titleRow=null;
		
		for(int i=0; i<titleArray.length; i++) {
			if(titleArray[i].equals("二维码")){
				continue;
			}
			if(cellIndex > 9){
				cellIndex=0;
				rowIndex++;
				titleRow = sheet.createRow(rowIndex);//下标为2的行开始
				titleRow.setHeight((short)540);
				CellRangeAddress dCra=new CellRangeAddress(rowIndex, rowIndex, 1, 3);
		    	sheet.addMergedRegion(dCra);
		    	dCra=new CellRangeAddress(rowIndex, rowIndex, 5, 6);
		    	sheet.addMergedRegion(dCra);
		    	dCra=new CellRangeAddress(rowIndex, rowIndex, 7, 8);
		    	sheet.addMergedRegion(dCra);
		    	if(rowIndex==3){
		    		dCra=new CellRangeAddress(rowIndex-1, rowIndex, 9, 9);
			    	sheet.addMergedRegion(dCra);
		    		dCra=new CellRangeAddress(rowIndex-1, rowIndex, 10, 10);
			    	sheet.addMergedRegion(dCra);
		    	}
			}else{
				titleRow = sheet.getRow(rowIndex);
			}
			
			
			if(cellIndex==0){
				cell = titleRow.createCell(cellIndex);
				cell.setCellStyle(dStyle);
				cell.setCellValue(new HSSFRichTextString(titleArray[i]));
				
				cellIndex = 1;
				
				cell = titleRow.createCell(cellIndex);
				cell.setCellStyle(dStyle);
				cell.setCellValue(new HSSFRichTextString(header.get(titleArray[i]).toString()));
				
				cellIndex = 4;
			}else if(cellIndex==4){
				cell = titleRow.createCell(cellIndex);
				cell.setCellStyle(dStyle);
				cell.setCellValue(new HSSFRichTextString(titleArray[i]+ "  " +header.get(titleArray[i]).toString()));
				
				cellIndex =5;
			}else if(cellIndex==5){
				cell = titleRow.createCell(cellIndex);
				cell.setCellStyle(dStyle);
				cell.setCellValue(new HSSFRichTextString(titleArray[i]));
				
				cellIndex =7;
				
				cell = titleRow.createCell(cellIndex);
				cell.setCellStyle(dStyle);
				cell.setCellValue(new HSSFRichTextString(header.get(titleArray[i]).toString()));
				
				cellIndex =9;
			}else if(cellIndex==9){
				if(rowIndex==2){
					cell = titleRow.createCell(cellIndex);
					cell.setCellStyle(dStyle);
					cell.setCellValue(new HSSFRichTextString(titleArray[i]));
				
					cellIndex =10;
			        
					cell = titleRow.createCell(cellIndex);
					cell.setCellStyle(dStyle);
					cell.setCellValue(new HSSFRichTextString(header.get(titleArray[i]).toString()));
				}else{
					cellIndex =10;
				}

			}
		}
		
    	return rowIndex;
	}
	
	/**
	 * 设置页脚
	 */
	@SuppressWarnings("deprecation")
	public int createFooter(Workbook workbook, List<Map<String, Object>> list, List<String> orderList,int count){
		Sheet sheet = workbook.getSheetAt(0);
		sheet.createRow(count);
		count++;
		Row row = sheet.createRow(count);
		row.setHeight((short)540);
		
    	CellStyle dStyle = workbook.createCellStyle();
		dStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		dStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		Cell cell = row.createCell(0);
		cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("送货人"));
		
		CellRangeAddress dCra=new CellRangeAddress(count, count, 1, 3);
    	sheet.addMergedRegion(dCra);
    	
    	cell = row.createCell(4);
    	cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("收货人"));
    	
		dCra=new CellRangeAddress(count, count, 5, 7);
    	sheet.addMergedRegion(dCra);
    	
    	cell = row.createCell(8);
    	cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("收货日期"));
		
		return count;
	}
	
	/**
	 * 设置头List
	 */
	public Map<String, String> getHeaderList(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("qr","二维码");
		map.put("vkorg","采购单位:");
		map.put("vkgrp","采购组:");
		map.put("date","计划送货日期:");
		map.put("remarks","备注:");
		map.put("kunnr","供货单位:");
		map.put("user","供货商联系人:");
		map.put("tel","联系电话:");
		return map;
	}
	
	
	/**
	 * 设置头顺序List
	 */
	public List<String> getHeaderOrderList(){
		List<String> orderList = new ArrayList<String>();
		orderList.add("二维码");
		orderList.add("采购单位:");
		orderList.add("采购组:");
		orderList.add("计划送货日期:");
		orderList.add("备注:");
		orderList.add("供货单位:");
		orderList.add("供货商联系人:");
		orderList.add("联系电话:");
		return orderList;
	}
	
	/**
	 * 设置页尾List
	 */
	public Map<String, String> getFooterList(){
		return null;
	}
	
	
	/**
	 * 设置页尾顺序List
	 */
	public List<String> getFooterOrderList(){
		return null;
	}
	
	/**
	 * 关系List
	 * @date 2017年9月27日
	 * @author 高海涛
	 * @return
	 */
	public Map<String, String> getDataMappingList() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("id","序号");
		map.put("ebeln","采购订单号");
		map.put("ebelp","行号");
		map.put("matnr","物料编码");
		map.put("maktx","物料描述");
		map.put("meins","单位");
		map.put("zddsl","订单数量");
		map.put("menge","计划送货数量");
		map.put("lgort","库存地点");
		map.put("number","需求跟踪号");
		map.put("ztxt","备注");
		
		return map;
	 }
	
	/**
	 * 排序List
	 * @date 2017年9月27日
	 * @author 高海涛
	 * @return
	 */
	public List<String> getOrderList() {
		List<String> orderList = new ArrayList<String>();
		
		orderList.add("序号");
		orderList.add("采购订单号");
		orderList.add("行号");
		orderList.add("物料编码");
		orderList.add("物料描述");
		orderList.add("单位");
		orderList.add("订单数量");
		orderList.add("计划送货数量");
		orderList.add("库存地点");
		orderList.add("需求跟踪号");
		orderList.add("备注");

		return orderList;
	 }
	
	/**
     * 设置列宽List
     */
	public Map<String,Integer> getWidthList(){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("序号",8);
		map.put("采购订单号",12);
		map.put("行号",8);
		map.put("物料编码",12);
		map.put("物料描述",30);
		map.put("单位",8);
		map.put("订单数量",8);
		map.put("计划送货数量",12);
		map.put("库存地点",8);
		map.put("需求跟踪号",12);
		map.put("备注",10);
		return map;
	}
	
}

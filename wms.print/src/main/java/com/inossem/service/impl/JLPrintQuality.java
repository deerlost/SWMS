package com.inossem.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.inossem.core.PrintBaseCreate;
import com.inossem.service.PrintService;

/** 
* @author 高海涛 
* @version 2017年9月27日
* 质检
*/
public class JLPrintQuality extends PrintBaseCreate implements PrintService {

	/**
	 * 生成打印文件
	 */
	@SuppressWarnings("unchecked")
	public String mdPrint(@SuppressWarnings("rawtypes") List<Map> dataList,String type){
		String url = "";
		
		List<Map<String,Object>> list = (List<Map<String,Object>>) dataList.get(0).get("list");
		
		try {
			url = getExcelReportUrl("质检单", list, getDataMappingList(), getOrderList());
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
		Row row = sheet.createRow(0);
		row.setHeight((short)560);
		CellStyle hStyle =this.getHeaderCellStyle(workbook);
		Cell c = row.createCell(0);
    	c.setCellValue(new HSSFRichTextString("中国石化股份有限公司-金陵分公司"));
    	c.setCellStyle(hStyle);
    	CellRangeAddress hCra=new CellRangeAddress(0, 0, 0, 11);
    	sheet.addMergedRegion(hCra);
    	
		row =sheet.createRow(1);
    	row.setHeight((short)560);
    	hStyle =this.getHeaderCellStyle(workbook);
    	c = row.createCell(0);
    	c.setCellValue(new HSSFRichTextString("入库检测委托单"));
    	c.setCellStyle(hStyle);
    	hCra=new CellRangeAddress(1, 1, 0, 11);
    	sheet.addMergedRegion(hCra);
    	
		CellStyle dStyle = workbook.createCellStyle();
		dStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		dStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		Row titleRow = sheet.createRow(2);//下标为2的行开始
		titleRow.setHeight((short)540);
		
		Cell cell = titleRow.createCell(0);
		cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("库房"));
		
		cell = titleRow.createCell(2);
		cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("委托检测单位"));
		
		hCra=new CellRangeAddress(2, 2, 3, 5);
    	sheet.addMergedRegion(hCra);
    	
    	cell = titleRow.createCell(6);
		cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("委托日期"));
		
		
		cell = titleRow.createCell(8);
		cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("委托单编号"));
		
		hCra=new CellRangeAddress(2, 2, 9, 11);
    	sheet.addMergedRegion(hCra);
    	
    	return 2;
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
		cell.setCellValue(new HSSFRichTextString("检测单位检测人"));
		
		CellRangeAddress dCra=new CellRangeAddress(count, count, 0, 1);
    	sheet.addMergedRegion(dCra);
    	dCra=new CellRangeAddress(count, count, 2, 6);
    	sheet.addMergedRegion(dCra);
    	
    	cell = row.createCell(7);
    	cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("物装中心保管员"));
    	
		dCra=new CellRangeAddress(count, count, 8, 11);
    	sheet.addMergedRegion(dCra);
		
		return count;
	}
	/**
	 * 设置头List
	 */
	public Map<String, String> getHeaderList(){
		return null;
	}
	
	
	/**
	 * 设置头顺序List
	 */
	public List<String> getHeaderOrderList(){
		return null;
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
		map.put("matnr","物料编码");
		map.put("maktx","物料描述");
		map.put("menge","数量");
		map.put("erpnum","ERP订单号");
		map.put("number","需求计划号");
		map.put("vname","供应商");
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
		orderList.add("物料编码");
		orderList.add("物料描述");
		orderList.add("材质");
		orderList.add("数量");
		orderList.add("炉批号");
		orderList.add("ERP订单号");
		orderList.add("需求计划号");
		orderList.add("供应商");
		orderList.add("光谱");
		orderList.add("硬度");
		orderList.add("超声");
		
		return orderList;
	 }
	
	/**
     * 设置列宽List
     */
	public Map<String,Integer> getWidthList(){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("序号",8);
		map.put("物料编码",12);
		map.put("物料描述",30);
		map.put("材质",12);
		map.put("数量",8);
		map.put("炉批号",12);
		map.put("ERP订单号",12);
		map.put("需求计划号",15);
		map.put("供应商",20);
		map.put("光谱",12);
		map.put("硬度",12);
		map.put("超声",12);
		return map;
	}
	
}

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
* YT盘点打印
*/
public class YTPrintInventory extends PrintBaseCreate implements PrintService {

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
			url = getExcelReportUrl("盘点表", list, getDataMappingList(), getOrderList(), header, getHeaderList(),
					getHeaderOrderList());
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
    	row.setHeight((short)540);
    	CellStyle hStyle =this.getHeaderCellStyle(workbook);
    	Cell c = row.createCell(0);
    	c.setCellValue(new HSSFRichTextString("伊泰股份工厂"));
    	c.setCellStyle(hStyle);
    	CellRangeAddress hCra=new CellRangeAddress(0, 0, 0, 12);
    	sheet.addMergedRegion(hCra);
    	
    	Map<String,Object> header = list.get(0);
    	String[] titleArray = getTitleArray(list, orderList);
    	
		CellStyle dStyle = workbook.createCellStyle();;
		dStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		dStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		int cellIndex = 12;	//大于11为新增行
		int rowIndex = 0;
		Cell cell =null;
		Row titleRow=null;
		
		for(int i=0; i<titleArray.length; i++) {
			
			if(cellIndex > 11){
				cellIndex=0;
				rowIndex++;
				titleRow = sheet.createRow(rowIndex);//下标为1的行开始
				titleRow.setHeight((short)540);
				CellRangeAddress dCra=new CellRangeAddress(rowIndex, rowIndex, 0, 1);
		    	sheet.addMergedRegion(dCra);
		    	dCra=new CellRangeAddress(rowIndex, rowIndex, 6, 8);
		    	sheet.addMergedRegion(dCra);
			}else{
				titleRow = sheet.getRow(rowIndex);
			}
	    	
			cell = titleRow.createCell(cellIndex);
			cell.setCellStyle(dStyle);
			cell.setCellValue(new HSSFRichTextString(titleArray[i]));
			if(cellIndex==0){
				cellIndex = 2;
			}else if(cellIndex==5){
				cellIndex =6;
			}else if(cellIndex==10){
				cellIndex =11;
			}
			cell = titleRow.createCell(cellIndex);
			cell.setCellStyle(dStyle);
			cell.setCellValue(new HSSFRichTextString(header.get(titleArray[i]).toString()));
			
			if(cellIndex==2){
				cellIndex = 5;
			}else if(cellIndex==6){
				cellIndex =10;
			}else if(cellIndex==11){
				cellIndex =12;
			}
		}
		
    	return rowIndex;
	}
	
	public int createFooter(Workbook workbook, List<Map<String, Object>> list, List<String> orderList,int count){
		return count;
	}
	
	/**
	 * 设置头List
	 */
	public Map<String, String> getHeaderList(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("IVNUM","盘点凭证号");
		map.put("PDATU","计划盘点日期");
		map.put("USNAM","盘点员");
		map.put("ZKCZT","库存状态");
		map.put("LGORT","库存地点");
		map.put("KZINV","盘点方式");

		return map;
	}
	
	
	/**
	 * 设置头顺序List
	 */
	public List<String> getHeaderOrderList(){
		List<String> orderList = new ArrayList<String>();
		orderList.add("盘点凭证号");
		orderList.add("计划盘点日期");
		orderList.add("盘点员");
		orderList.add("库存状态");
		orderList.add("库存地点");
		orderList.add("盘点方式");

		return orderList;
	}
	
	/**
	 * 关系List
	 * @date 2017年9月27日
	 * @author 高海涛
	 * @return
	 */
	public Map<String, String> getDataMappingList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ID","序号");
		map.put("MATNR","物料编码");
		map.put("MAKTX","名称");
		map.put("MEINS","单位");
		map.put("SOBKZ","库存标示");
		map.put("ZSO","特殊库存编号-描述");
		map.put("WDATU","入库日期");
		map.put("LGTYP","仓储区");
		map.put("LGPLA","仓位");
		map.put("CHARG","批次号");
		map.put("GESME","账面");
		map.put("MENGE","盘点");
		map.put("STATUS","盘盈亏情况");

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
		orderList.add("名称");
		orderList.add("单位");
		orderList.add("库存标示");
		orderList.add("特殊库存编号-描述");
		orderList.add("入库日期");
		orderList.add("仓储区");
		orderList.add("仓位");
		orderList.add("批次号");
		orderList.add("账面");
		orderList.add("盘点");
		orderList.add("盘盈亏情况");

		return orderList;
	 }
	
	/**
     * 设置列宽List
     */
	public Map<String,Integer> getWidthList(){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("序号",4);
		map.put("物料编码",10);
		map.put("名称",25);
		map.put("单位",4);
		map.put("库存标示",8);
		map.put("特殊库存编号-描述",16);
		map.put("入库日期",8);
		map.put("仓储区",8);
		map.put("仓位",10);
		map.put("批次号",10);
		map.put("账面",8);
		map.put("盘点",8);
		map.put("盘盈亏情况",8);
		return map;
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
	
}

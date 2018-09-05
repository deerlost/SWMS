package com.inossem.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.inossem.core.PrintBaseCreate;
import com.inossem.service.PrintService;

/** 
* @author 高海涛 
* @version 2017年9月27日
* JL到出库单
*/
public class JLPrintOutOrder extends PrintBaseCreate implements PrintService {

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
			url = getExcelReportUrl("出库单",
					list,getDataMappingList(),getOrderList(),
					header,getHeaderList(),getHeaderOrderList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	

	//重写 设置工作页打印设置
	public void setSheetPrintSetup(Workbook workbook, List<Map<String, Object>> list){
		PrintSetup print = workbook.getSheetAt(0).getPrintSetup();
    	print.setLandscape(false);//横向
    	print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);//A4纸张
    	print.setFitWidth((short) 1);//将所有列调整为一页
    	print.setFitHeight((short) 0);//将所有列调整为一页（0表示不设置页数）
    	
    	HSSFFooter footer = (HSSFFooter )workbook.getSheetAt(0).getFooter() ;
    	footer.setCenter( "Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages() ); 
		
	}
	/**
	 * 实现头信息方法
	 */
	@SuppressWarnings("deprecation")
	public int createHeader(Workbook workbook, List<Map<String, Object>> list, List<String> orderList){
		Sheet sheet = workbook.getSheetAt(0);
		
		//第一行
		Row row = sheet.createRow(0);
		row.setHeight((short)560);
		CellStyle hStyle =this.getHeaderCellStyle(workbook);
		Cell c = row.createCell(0);
    	c.setCellValue(new HSSFRichTextString("中国石化股份有限公司-金陵分公司"));
    	c.setCellStyle(hStyle);
    	CellRangeAddress hCra=new CellRangeAddress(0, 0, 0, 7);
    	sheet.addMergedRegion(hCra);
    	
    	//第二行
		row =sheet.createRow(1);
    	row.setHeight((short)560);
    	c = row.createCell(0);
    	c.setCellValue(new HSSFRichTextString("物装物资出库单"));
    	c.setCellStyle(hStyle);
    	hCra=new CellRangeAddress(1, 1, 0, 7);
    	sheet.addMergedRegion(hCra);
    	
    	Map<String,Object> header = list.get(0);
    	String[] titleArray = getTitleArray(list, orderList);
		//头信息
		CellStyle dStyle = workbook.createCellStyle();
		dStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		dStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		int cellIndex = 4;	//大于3为新增行
		int rowIndex = 1;
		Cell cell =null;
		Row titleRow=null;
		
		for(int i=0; i<titleArray.length; i++) {
			if(cellIndex > 3){
				cellIndex=0;
				rowIndex++;
				titleRow = sheet.createRow(rowIndex);//下标为2的行开始
				titleRow.setHeight((short)540);
				CellRangeAddress dCra=new CellRangeAddress(rowIndex, rowIndex, 1, 2);
		    	sheet.addMergedRegion(dCra);
		    	if(rowIndex!=3 && rowIndex!=4){
		    		dCra=new CellRangeAddress(rowIndex, rowIndex, 4, 7);
			    	sheet.addMergedRegion(dCra);
		    	}
		    	
		    	if(rowIndex==4){
		    		dCra=new CellRangeAddress(rowIndex-1, rowIndex, 3, 3);
			    	sheet.addMergedRegion(dCra);
			    	dCra=new CellRangeAddress(rowIndex-1, rowIndex, 4, 7);
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
				
				cellIndex = 3;
			}else if(cellIndex==3){
				if(rowIndex!=4){
					cell = titleRow.createCell(cellIndex);
					cell.setCellStyle(dStyle);
					cell.setCellValue(new HSSFRichTextString(titleArray[i]));
					
					cellIndex = 4;
					
					cell = titleRow.createCell(cellIndex);
					cell.setCellStyle(dStyle);
					cell.setCellValue(new HSSFRichTextString(header.get(titleArray[i]).toString()));
				}else{
					cellIndex = 4;
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
		cell.setCellValue(new HSSFRichTextString("领料人"));
		
		CellRangeAddress dCra=new CellRangeAddress(count, count, 1, 2);
    	sheet.addMergedRegion(dCra);
    	
    	cell = row.createCell(3);
    	cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("发货人"));
    	
		dCra=new CellRangeAddress(count, count, 4, 5);
    	sheet.addMergedRegion(dCra);
    	
    	cell = row.createCell(6);
    	cell.setCellStyle(dStyle);
		cell.setCellValue(new HSSFRichTextString("日期"));
		
		return count;
	}
	
	/**
	 * 设置头List
	 */
	public Map<String, String> getHeaderList(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("type","领料申请类型");
		map.put("werks","提报单位");
		map.put("mblnr","凭证编号");
		map.put("addr","配送地址");
		map.put("date","记账日期");
		map.put("user","联系人");
		map.put("tel","联系电话");
		return map;
	}
	
	
	/**
	 * 设置头顺序List
	 */
	public List<String> getHeaderOrderList(){
		List<String> orderList = new ArrayList<String>();
		orderList.add("领料申请类型");
		orderList.add("提报单位");
		orderList.add("凭证编号");
		orderList.add("配送地址");
		orderList.add("记账日期");
		orderList.add("联系人");
		orderList.add("联系电话");
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
		map.put("matnr","物料编码");
		map.put("maktx","物料描述");
		map.put("meins","单位");
		map.put("menge","数量");
		map.put("lgort","库存地点");
		map.put("lgpla","仓库");
		map.put("remark","工单/网络/预留");
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
		orderList.add("单位");
		orderList.add("数量");
		orderList.add("库存地点");
		orderList.add("仓库");
		orderList.add("工单/网络/预留");

		return orderList;
	 }
	
	/**
     * 设置列宽List
     */
	public Map<String,Integer> getWidthList(){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("序号",12);
		map.put("物料编码",15);
		map.put("物料描述",30);
		map.put("单位",8);
		map.put("数量",8);
		map.put("库存地点",8);
		map.put("仓库",8);
		map.put("工单/网络/预留",15);
		return map;
	}
	
}

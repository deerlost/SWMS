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
 * @Package com.inossem.service.impl
 * @ClassName：CWPrintProTransport
 * @Description :生产转运单打印
 * @anthor：王洋
 * @date：2018年6月12日 上午10:29:42 @版本：V1.0
 */
public class CWPrintProTransport extends PrintBaseCreate implements PrintService {

	@SuppressWarnings("unchecked")
	@Override
	public String mdPrint(@SuppressWarnings("rawtypes") List<Map> dataList, String type) {
		String url = "";
		String pdfUrl = "";

		List<Map<String, Object>> list = (List<Map<String, Object>>) dataList.get(0).get("list");

		List<Map<String, Object>> header = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = (Map<String, Object>) dataList.get(0).get("header");
		header.add(map);

		try {
			url = getExcelReportUrl("转运生产单", list, getDataMappingList(), getOrderList(), header, getHeaderList(),
					getHeaderOrderList());
			pdfUrl = super.getPdfReportUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdfUrl;
	}

	/**
	 * 实现头信息方法
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int createHeader(Workbook workbook, List<Map<String, Object>> list, List<String> orderList) {
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.createRow(0);
		row.setHeight((short) 540);
		CellStyle hStyle = this.getHeaderCellStyle(workbook);
		Cell c = row.createCell(0);
		c.setCellValue(new HSSFRichTextString("川维股份工厂"));
		c.setCellStyle(hStyle);
		CellRangeAddress hCra = new CellRangeAddress(0, 0, 0, 12);
		sheet.addMergedRegion(hCra);

		Map<String, Object> header = list.get(0);
		String[] titleArray = getTitleArray(list, orderList);

		CellStyle dStyle = workbook.createCellStyle();
		;
		dStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		dStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		int cellIndex = 12; // 大于11为新增行
		int rowIndex = 0;
		Cell cell = null;
		Row titleRow = null;

		for (int i = 0; i < titleArray.length; i++) {

			if (cellIndex > 11) {
				cellIndex = 0;
				rowIndex++;
				titleRow = sheet.createRow(rowIndex);// 下标为1的行开始
				titleRow.setHeight((short) 540);
				CellRangeAddress dCra = new CellRangeAddress(rowIndex, rowIndex, 0, 1);
				sheet.addMergedRegion(dCra);
				dCra = new CellRangeAddress(rowIndex, rowIndex, 6, 8);
				sheet.addMergedRegion(dCra);
			} else {
				titleRow = sheet.getRow(rowIndex);
			}

			cell = titleRow.createCell(cellIndex);
			cell.setCellStyle(dStyle);
			cell.setCellValue(new HSSFRichTextString(titleArray[i]));
			if (cellIndex == 0) {
				cellIndex = 2;
			} else if (cellIndex == 5) {
				cellIndex = 6;
			} else if (cellIndex == 10) {
				cellIndex = 11;
			}
			cell = titleRow.createCell(cellIndex);
			cell.setCellStyle(dStyle);
			cell.setCellValue(new HSSFRichTextString(header.get(titleArray[i]).toString()));

			if (cellIndex == 2) {
				cellIndex = 5;
			} else if (cellIndex == 6) {
				cellIndex = 10;
			} else if (cellIndex == 11) {
				cellIndex = 12;
			}
		}

		return rowIndex;
	}

	@Override
	public int createFooter(Workbook workbook, List<Map<String, Object>> list, List<String> orderList, int count) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, String> getHeaderList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("FAC", "发出工厂");
		map.put("LOC", "发出库存地点");
		map.put("DATE", "日期");
		map.put("USER", "创建人");
		map.put("DRI", "司机");
		map.put("VEH", "运输车辆");
		map.put("REMARK", "备注");
		return map;
	}

	@Override
	public List<String> getHeaderOrderList() {
		List<String> orderList = new ArrayList<String>();
		orderList.add("发出工厂");
		orderList.add("发出库存地点");
		orderList.add("日期");
		orderList.add("创建人");
		orderList.add("司机");
		orderList.add("运输车辆");
		orderList.add("备注");
		return orderList;
	}

	@Override
	public Map<String, String> getFooterList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getFooterOrderList() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * <p>Title: getDataMappingList</br></p> <p>关系List: </p>
	 * 
	 * @return</br>
	 * 
	 * @see com.inossem.core.PrintBaseCreate#getDataMappingList()</br>
	 */
	@Override
	public Map<String, String> getDataMappingList() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("SPE", "品名规格型号");
		map.put("PROBA", "生产批次");
		map.put("PACK", "包装及规格");
		map.put("TQTY", "转运数量");
		map.put("INFTY", "接收工厂");
		map.put("INLOCs", "接收库存地点");
		return map;
	}

	/*
	 * <p>Title: getOrderList</br></p> <p>排序List: </p>
	 * 
	 * @return</br>
	 * 
	 * @see com.inossem.core.PrintBaseCreate#getOrderList()</br>
	 */
	@Override
	public List<String> getOrderList() {
		List<String> orderList = new ArrayList<String>();
		orderList.add("品名规格型号");
		orderList.add("生产批次");
		orderList.add("包装及规格");
		orderList.add("转运数量");
		orderList.add("接收工厂");
		orderList.add("接收库存地点");
		return orderList;
	}

	/**
	 * 设置列宽List
	 */
	@Override
	public Map<String, Integer> getWidthList() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("品名规格型号",4);
		map.put("生产批次",10);
		map.put("包装及规格",25);
		map.put("转运数量",4);
		map.put("接收工厂",8);
		map.put("接收库存地点",16);
		return map;
	}

}

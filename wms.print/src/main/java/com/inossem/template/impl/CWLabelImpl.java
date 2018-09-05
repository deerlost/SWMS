package com.inossem.template.impl;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.inossem.core.Constant;
import com.inossem.core.JbarcodeUtil2;
import com.inossem.core.PrintBaseCopy;
import com.inossem.core.QRcodeUtil;
import com.inossem.template.CopyTemplate;

public class CWLabelImpl extends PrintBaseCopy implements CopyTemplate{
	
	@Override
	public HSSFWorkbook makeExcel(HSSFWorkbook hssInputExcelFile, @SuppressWarnings("rawtypes") List<Map> dataList,String labelType) {
		int startCol = 0;	//开始列
		int endCol = 5;		//结束列
		int startRow = 0;	//开始行
		int endRow = 17;	//结束行
		int readPlace = 0;	//位置
		
		HSSFSheet hssInputExcelSheet = hssInputExcelFile.getSheetAt(0);
		String inputSheetName = hssInputExcelFile.getSheetName(0);
		String outSheetName = "print";
		HSSFSheet hssOutputExcelSheet = hssInputExcelFile.createSheet(outSheetName);
		
		for(int i = 0; i < dataList.size(); i++){
			@SuppressWarnings("unchecked")
			Map<String,Object> printData = (Map<String,Object>)dataList.get(i);
			String content ="";
			//复制格式
			this.mdRangeCopy(inputSheetName, outSheetName, startRow, endRow, startCol,endCol,readPlace, hssInputExcelFile);
			
			//二维码路径
			String imagePath = this.getOutPutQRcodeFilePath()+"_"+i+ Constant.OUTPUTFILE_TYPE_PNG;
			
			if(labelType.equals(Constant.TYPE_1)){
				//打印文本
				setPalletExcelDate(hssOutputExcelSheet, startRow, readPlace, printData);
				//生成条形码
				content =Constant.ANDROID_TYPE_1 + printData.get("托盘号").toString();
				JbarcodeUtil2.encode(content,40,30,imagePath);
				this.imageSet(hssInputExcelFile, hssOutputExcelSheet,imagePath,readPlace,1,1,5,10);
			}
			if(labelType.equals(Constant.TYPE_3)){
				//打印文本
				setPositionExcelDate(hssOutputExcelSheet,startRow,readPlace,printData);
				//生成条形码
				content =Constant.ANDROID_TYPE_3 + printData.get("仓库号-储存区-仓位号").toString();
				JbarcodeUtil2.encode(content,40,30,imagePath);
				this.imageSet(hssInputExcelFile, hssOutputExcelSheet,imagePath,readPlace,1,1,5,10);
			}
			if(labelType.equals(Constant.TYPE_2)){
				//打印文本
				setPackageExcelDate(hssOutputExcelSheet, startRow, readPlace, printData);
				//生成二维码
				content = printData.get("包装物SN").toString();
				QRcodeUtil.zxingCodeCreate(content,400,400,imagePath,"png");
				this.imageSet(hssInputExcelFile, hssOutputExcelSheet,imagePath,readPlace,1,1,4,17);
			}
			readPlace = readPlace+ 17;
			hssOutputExcelSheet.setRowBreak(readPlace-1);
		}
		
		// 打印设置
		hssInputExcelFile.setPrintArea(1, 0, endCol, 0, readPlace-1);
		this.printSetLabel(hssInputExcelSheet, hssOutputExcelSheet);
		hssInputExcelFile.removeSheetAt(0);
		return hssInputExcelFile;
	}

	/**
	 * 获取路径
	 * @date 2017年11月1日
	 * @author 高海涛
	 * @return
	 */
	public String getPath(){
		return this.getOutPutFilePath();
	}
	
	/**
	 * 获取二维码路径
	 * @date 2017年11月30日
	 * @author 高海涛
	 * @return
	 */
	public String getQRPath(){
		return this.getOutPutQRcodeFilePath();
	}
	
	
	/**
	 * 仓位标签
	 * @param hssOutputExcelSheet
	 * @param startRow
	 * @param readPlace
	 * @param printData
	 */
	public void setPositionExcelDate(HSSFSheet hssOutputExcelSheet,int startRow,int readPlace,Map<String,Object> printData){
		HSSFRow hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+11);
		hssDetalSet.getCell(1).setCellValue(printData.get("仓库号-储存区-仓位号").toString());
	}

	/**
	 * 托盘标签
	 * @param hssOutputExcelSheet
	 * @param startRow
	 * @param readPlace
	 * @param printData
	 */
	public void setPalletExcelDate(HSSFSheet hssOutputExcelSheet,int startRow,int readPlace,Map<String,Object> printData){
		HSSFRow hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+11);
		hssDetalSet.getCell(1).setCellValue(printData.get("托盘号").toString());
	}

	/**
	 * 包装物标签
	 * @param hssOutputExcelSheet
	 * @param startRow
	 * @param readPlace
	 * @param printData
	 */
	public void setPackageExcelDate(HSSFSheet hssOutputExcelSheet,int startRow,int readPlace,Map<String,Object> printData){
		HSSFRow hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+17);
		String c = printData.get("包装物SN").toString().split("SN")[1].replaceAll("=", "");
		hssDetalSet.getCell(1).setCellValue(c);
	}
	@Override
	public String mdPrint(@SuppressWarnings("rawtypes") List<Map> dataList, String type) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}

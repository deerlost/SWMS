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

/** 
* @author 高海涛 
* @version 2017年9月21日
* 标签打印 
*/
public class YTLabelImpl extends PrintBaseCopy implements CopyTemplate {

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
				setChargExcelDate(hssOutputExcelSheet,startRow,readPlace,printData);
				//生成二维码
				content =Constant.ANDROID_TYPE_1 + setQRcodeContent(printData);
				QRcodeUtil.zxingCodeCreate(content,400,400,imagePath,"png");
				this.imageSet(hssInputExcelFile, hssOutputExcelSheet,imagePath,readPlace,1,1,3,10);
			}else if(labelType.equals(Constant.TYPE_2)){
				//打印文本
				setMaterielExcelDate(hssOutputExcelSheet,startRow,readPlace,printData);
				//生成2个条形码 
				content =Constant.ANDROID_TYPE_2 + printData.get("物料编号").toString();
				JbarcodeUtil2.encode(content,40,30,imagePath);
				this.imageSet(hssInputExcelFile, hssOutputExcelSheet,imagePath,readPlace,1,1,6,4);
				String imagePath2 = this.getOutPutQRcodeFilePath()+"_"+i+"_"+i+ Constant.OUTPUTFILE_TYPE_PNG;
				content =Constant.ANDROID_TYPE_3 + printData.get("采购订单号").toString(); 
				JbarcodeUtil2.encode(content,40,30,imagePath2);
				this.imageSet(hssInputExcelFile, hssOutputExcelSheet,imagePath2,readPlace,3,15,6,16);
			}else if(labelType.equals(Constant.TYPE_3)){
				//打印文本
				setPositionExcelDate(hssOutputExcelSheet,startRow,readPlace,printData);
				//生成条形码
				content =Constant.ANDROID_TYPE_4 + printData.get("储存区-仓位号").toString();
				JbarcodeUtil2.encode(content,40,30,imagePath);
				this.imageSet(hssInputExcelFile, hssOutputExcelSheet,imagePath,readPlace,1,1,5,10);
			}else if(labelType.equals(Constant.TYPE_4)){
				//打印文本
				setJJCRKExcelDate(hssOutputExcelSheet,startRow,readPlace,printData);
				//生成条形码
				content =Constant.ANDROID_TYPE_5 + printData.get("订单号").toString();
				JbarcodeUtil2.encode(content,40,30,imagePath);
				this.imageSet(hssInputExcelFile, hssOutputExcelSheet,imagePath,readPlace,1,7,6,9);
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
	 * 设置二维码内容
	 * @date 2017年9月29日
	 * @author 高海涛
	 */
	public String setQRcodeContent(Map<String,Object> printData){
		return "" + printData.get("批次号") + "$$" +
					printData.get("采购订单") + "$$" +
					printData.get("供应商") + "$$" +
					printData.get("合同号") + "$$" + 
					printData.get("批次号") + "$$" +
					printData.get("物料编号") + "$$" + 
					printData.get("物料描述") + "$$" +
					printData.get("入库时间");
	}
	/**
	 * 批次打印set data
	 * @date 2017年9月22日
	 * @author 高海涛
	 * @param hssOutputExcelSheet
	 * @param startRow
	 * @param readPlace
	 * @param printData
	 */
	public void setChargExcelDate(HSSFSheet hssOutputExcelSheet,int startRow,int readPlace,Map<String,Object> printData){
		HSSFRow hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+1);
		hssDetalSet.getCell(5).setCellValue(printData.get("批次号").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+3);
		hssDetalSet.getCell(5).setCellValue(printData.get("采购订单").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+5);
		hssDetalSet.getCell(5).setCellValue(printData.get("入库时间").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+7);
		hssDetalSet.getCell(5).setCellValue(printData.get("合同号").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+9);
		hssDetalSet.getCell(5).setCellValue(printData.get("需求部门").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+11);
		hssDetalSet.getCell(2).setCellValue(printData.get("物料编号").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+13);
		hssDetalSet.getCell(2).setCellValue(printData.get("物料描述").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+15);
		hssDetalSet.getCell(2).setCellValue(printData.get("供应商").toString());
	}
	
	/**
	 * 物料标签 set data
	 * @date 2017年9月22日
	 * @author 高海涛
	 * @param hssOutputExcelSheet
	 * @param startRow
	 * @param readPlace
	 * @param printData
	 */
	public void setMaterielExcelDate(HSSFSheet hssOutputExcelSheet,int startRow,int readPlace,Map<String,Object> printData){
		HSSFRow hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+5);
		hssDetalSet.getCell(3).setCellValue(printData.get("物料编号").toString() + printData.get("需求部门").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+6);
		hssDetalSet.getCell(3).setCellValue(printData.get("物料描述").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+7);
		hssDetalSet.getCell(3).setCellValue(printData.get("到货日期").toString());
		//hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+9);
		hssDetalSet.getCell(5).setCellValue(printData.get("到货数量").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+8);
		hssDetalSet.getCell(3).setCellValue(printData.get("供应商名称").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+10);
		hssDetalSet.getCell(3).setCellValue(printData.get("采购订单号/行号").toString());
	}
	
	/**
	 * 仓位标签
	 * @date 2017年9月25日
	 * @author 高海涛
	 * @param hssOutputExcelSheet
	 * @param startRow
	 * @param readPlace
	 * @param printData
	 */
	public void setPositionExcelDate(HSSFSheet hssOutputExcelSheet,int startRow,int readPlace,Map<String,Object> printData){
		HSSFRow hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+11);
		hssDetalSet.getCell(1).setCellValue(printData.get("储存区-仓位号").toString());
	}
	
	
	public void setJJCRKExcelDate(HSSFSheet hssOutputExcelSheet,int startRow,int readPlace,Map<String,Object> printData){
		HSSFRow hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+1);
		hssDetalSet.getCell(3).setCellValue(printData.get("需求部门").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+3);
		hssDetalSet.getCell(3).setCellValue(printData.get("责任人").toString());
		hssDetalSet = hssOutputExcelSheet.getRow(startRow+readPlace+5);
		hssDetalSet.getCell(3).setCellValue(printData.get("创建日期").toString());
	}

	@Override
	public String mdPrint(List<Map> dataList, String type) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

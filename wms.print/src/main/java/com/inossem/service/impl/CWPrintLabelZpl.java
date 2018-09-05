package com.inossem.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.inossem.core.Constant;
import com.inossem.core.PropertiesUtil;
import com.inossem.core.ZplPrint;
import com.inossem.service.PrintService;
import com.inossem.template.CopyTemplate;
import com.inossem.template.impl.CWLabelImpl;


public class CWPrintLabelZpl implements PrintService{
	
	/**
	 * 托盘打印
	 * @param data
	 * @return
	 */
	private boolean palletPrintForCW(Map<String,String> data) {
		ZplPrint zplPrint = new ZplPrint();
		
		zplPrint.zplInit(data.get("print_name"),550);
		
		String content = Constant.ANDROID_TYPE_1 + data.get("content");
		
		// 左右 上下  大小
		zplPrint.setCode128("180","138","2","150",content);
		
		int[] xy = new int[] { 200, 300 };

		xy = zplPrint.setLabelValue(xy,0, "托盘", data.get("content"));

		String zplString = zplPrint.getZplString();
		
		System.out.println("ZPL完整代码 : " + zplString);
		
		return zplPrint.createPrintJob(zplString);
	}
	
	/**
	 * 仓位打印
	 * @param data
	 * @return
	 */
	private boolean positionPrintForCW(Map<String,String> data) {
		ZplPrint zplPrint = new ZplPrint();
		
		zplPrint.zplInit(data.get("print_name"),550);
		String str = data.get("content");
		String content = Constant.ANDROID_TYPE_3 + str;
		// 左右 上下 大小		
		zplPrint.setCode128("168","138","2","150", content);
		
		int[] xy = new int[] { 200, 300 };

		xy = zplPrint.setLabelValue(xy,0, "仓位", data.get("content"));

		String zplString = zplPrint.getZplString();
		
		System.out.println("ZPL完整代码 : " + zplString);
		
		return zplPrint.createPrintJob(zplString);
	}
	
	
	/**
	 * 包装物打印
	 * @param data
	 * @return
	 */
	private boolean packagePrintForCW(Map<String,String> data) {
		ZplPrint zplPrint = new ZplPrint();
		
		zplPrint.zplInit(data.get("print_name"),710);
		String content = PropertiesUtil.getInstance().getInfo_url()+"?SN="+data.get("content");
		// 左右 上下 类型 大小						 	  			
		zplPrint.setQRcode("250","53","2","10",content);
		
		String zplString = zplPrint.getZplString();
		
		System.out.println("ZPL完整代码 : " + zplString);
		
		return zplPrint.createPrintJob(zplString);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String mdPrint(@SuppressWarnings("rawtypes") List<Map> dataList, String type) {
		String templateType="";	//标签模版
		@SuppressWarnings("rawtypes")
		List<Map> listInputData = new ArrayList<Map>();
		Boolean flag = false ;
		for(Map<String,String> data : dataList){
			if(type.equals(Constant.TYPE_1)){
				try {
					flag = this.palletPrintForCW(data);
					if(flag){
						flag = false;
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				//仓位标签
				listInputData.add(setMapToPalletLabel(data));
				templateType = Constant.TEMPLATE_LABEL_POSITION;
			}
			if(type.equals(Constant.TYPE_2)){
				try {
					flag = this.packagePrintForCW(data);
					if(flag){
						flag = false;
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				//包装物SN标签
				listInputData.add(setMapToPackegeLabel(data));
				templateType = Constant.TEMPLATE_LABEL_PACKAGESN;
			}
			if(type.equals(Constant.TYPE_3)){
				try {
					flag = this.positionPrintForCW(data);
					if(flag){
						flag = false;
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				//仓位标签
				listInputData.add(setMapToPositionLabel(data));
				templateType = Constant.TEMPLATE_LABEL_POSITION;
			}
		}
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String fileName = this.printLabelFor(listInputData,request.getServletContext().getRealPath(templateType),new CWLabelImpl(),type);
		return fileName;
	}

	/**
	 * 包装物SN标签
	 * @param temp
	 * @return
	 */
	public Map<String,String> setMapToPackegeLabel(Map<String,String> temp){
		Map<String,String> map = new HashMap<String,String>();
		String content = temp.get("content")==null?"":temp.get("content");
		content = PropertiesUtil.getInstance().getInfo_url()+"?SN="+content;
		map.put("包装物SN",content);
		return map;
	}

	/**
	 * 仓位标签
	 * @param temp
	 * @return
	 */
	public Map<String,String> setMapToPositionLabel(Map<String,String> temp){
		Map<String,String> map = new HashMap<String,String>();
		String content = temp.get("content")==null?"":temp.get("content");
		map.put("仓库号-储存区-仓位号",content) ;
		return map;
	}
	
	/**
	 * 托盘标签
	 * @param temp
	 * @return
	 */
	public Map<String,String> setMapToPalletLabel(Map<String,String> temp){
		Map<String,String> map = new HashMap<String,String>();
		String content = temp.get("content")==null?"":temp.get("content");
		map.put("托盘号",content) ;
		return map;
	}
	
	/**
	 *  打印文件生成
	 * @param dataList
	 * @param fileModePath
	 * @param reportImpl
	 * @param type
	 * @return
	 */
	private String printLabelFor(@SuppressWarnings("rawtypes") List<Map> dataList,String fileModePath,CopyTemplate reportImpl,String type) {

		CopyTemplate report = reportImpl;
		
		if (dataList.size() == 0) {
			return "";
		}

		String outPutFilePath = report.getPath();
		String fileName = ""+new Date().getTime();
		String outPutFilePath_xls = outPutFilePath + fileName + Constant.OUTPUTFILE_TYPE_XLS;
		//String outPutFilePath_pdf = outPutFilePath + fileName + Constant.OUTPUTFILE_TYPE_PDF;
		FileOutputStream fileOutput = null;
		
		try {
			
			POIFSFileSystem poiInputFile = new POIFSFileSystem( new FileInputStream(fileModePath));
			HSSFWorkbook hssInputExcelFile = report.makeExcel(new HSSFWorkbook(poiInputFile), dataList,type);
			
			// 打印文件生成
			fileOutput = new FileOutputStream(outPutFilePath_xls);
			hssInputExcelFile.write(fileOutput);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutput != null) {
				try {
					fileOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//PDF转换
        /*try {
        	this.getPdfRopertUrl(outPutFilePath_xls,outPutFilePath_pdf);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
        
        return fileName;
	}
}

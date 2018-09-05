package com.inossem.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import net.sf.json.JSONObject;

/** 
* @author 高海涛 
* @version 2017年9月18日
* 制作Excel表格
*/
public interface CopyTemplate {

	public HSSFWorkbook makeExcel(HSSFWorkbook hssInputExcelFile, @SuppressWarnings("rawtypes") List<Map> dataList,String type);

	public String getPath();
	
	public String getQRPath();
	

	@SuppressWarnings("rawtypes")
	String mdPrint(List<Map> dataList, String type) throws Exception;

}

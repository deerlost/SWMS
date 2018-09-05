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
import com.inossem.service.PrintService;
import com.inossem.template.CopyTemplate;
import com.inossem.template.impl.YTLabelImpl;


/**
 * YT标签打印
 * @author 高海涛
 */
public class YTPrintLabel implements PrintService{
	
	/**
	 * 打印文件生成
	 * @date 2017年9月18日
	 * @author 高海涛
	 * @param dataList
	 * @param fileModePath
	 * @param reportImpl
	 * @return
	 */
	private String printLabelForYt(@SuppressWarnings("rawtypes") List<Map> dataList,String fileModePath,CopyTemplate reportImpl,String type) {

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

	/**
	 * 打印文件设置
	 */
	public String mdPrint(@SuppressWarnings("rawtypes") List<Map> printData,String labelType){
		String templateType="";	//标签模版
		
		@SuppressWarnings("rawtypes")
		List<Map> listInputData = new ArrayList<Map>();
		
		for(int i=0;i<printData.size();i++){
			@SuppressWarnings("unchecked")
			Map<String,String> temp = printData.get(i);
			if(labelType.equals(Constant.TYPE_1)){
				//批次标签
				listInputData.add(setMapToChargLabel(temp));
				templateType = Constant.TEMPLATE_LABEL_CHAGR;
			}else if(labelType.equals(Constant.TYPE_2)){
				//物料标签
				listInputData.add(setMapToMaterielLabel(temp));
				templateType = Constant.TEMPLATE_LABEL_MATERIEL;
			}else if(labelType.equals(Constant.TYPE_3)){
				//仓位标签
				listInputData.add(setMapToPositionLabel(temp));
				templateType = Constant.TEMPLATE_LABEL_POSITION;
			}else if(labelType.equals(Constant.TYPE_4)){
				//紧急出入库
				listInputData.add(setMapToJJCRKLabel(temp));
				templateType = Constant.TEMPLATE_LABEL_JJCRK;
			}
			
		}
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String fileName = this.printLabelForYt(listInputData,request.getServletContext().getRealPath(templateType),new YTLabelImpl(),labelType);
		return fileName;
	}
	
	/**
	 * 批次标签
	 * @date 2017年9月22日
	 * @author 高海涛
	 * @param temp
	 * @return
	 */
	public Map<String,String> setMapToChargLabel(Map<String,String> temp){
		Map<String,String> map = new HashMap<String,String>();
		map.put("批次号", temp.get("batch")==null?"":temp.get("batch"));
		map.put("采购订单",temp.get("purchase_order_code")==null?"":temp.get("purchase_order_code"));
		map.put("供应商", temp.get("supplier_name")==null?"":temp.get("supplier_name"));
		map.put("合同号", temp.get("contract_code")==null?"":temp.get("contract_code"));
		map.put("需求部门", temp.get("demand_dept")==null?"":temp.get("demand_dept"));
		String spec_stock=temp.get("spec_stock")==null?"":temp.get("spec_stock");
		String spec_stock_code=temp.get("spec_stock_code")==null?"":temp.get("spec_stock_code");
		
		if(!"".equals(spec_stock)){
			spec_stock = "  "+spec_stock;
			if(!"".equals(spec_stock_code)){
				spec_stock=spec_stock+ "-" +spec_stock_code;
			}
		}
		
		map.put("物料编号", temp.get("mat_code")==null?"":temp.get("mat_code")+spec_stock);
		map.put("物料描述", temp.get("mat_name")==null?"":temp.get("mat_name"));
		map.put("入库时间", temp.get("input_date")==null?"":temp.get("input_date"));
		return map;
	}
	
	/**
	 * 物料标签
	 * @date 2017年9月22日
	 * @author 高海涛
	 * @param temp
	 * @return
	 */
	public Map<String,String> setMapToMaterielLabel(Map<String,String> temp){
		Map<String,String> map = new HashMap<String,String>();
		String qty = temp.get("qty")==null?"":temp.get("qty");
		String unit = temp.get("unit")==null?"":temp.get("unit");
		map.put("到货数量", qty +" " + unit);
		String purchase_order_code = temp.get("purchase_order_code")==null?"":temp.get("purchase_order_code");
		String purchase_order_rid = temp.get("purchase_order_rid")==null?"":temp.get("purchase_order_rid");
		map.put("采购订单号/行号", purchase_order_code +"/"+ purchase_order_rid);
		map.put("采购订单号", purchase_order_code);
		map.put("供应商名称", temp.get("supplier_name")==null?"":temp.get("supplier_name"));
		String dept = "";
		if(temp.get("dept")!=null){
			dept = "(" + temp.get("dept") + ")";
		}
		map.put("需求部门", dept);
		map.put("物料编号", temp.get("mat_code")==null?"":temp.get("mat_code"));
		map.put("物料描述", temp.get("mat_name")==null?"":temp.get("mat_name"));
		map.put("到货日期", temp.get("input_date")==null?"":temp.get("input_date"));
		return map;
	}
	
	/**
	 * 仓位标签
	 * @date 2017年9月25日
	 * @author 高海涛
	 * @param temp
	 * @return
	 */
	public Map<String,String> setMapToPositionLabel(Map<String,String> temp){
		Map<String,String> map = new HashMap<String,String>();
		String area_code = temp.get("area_code")==null?"":temp.get("area_code");
		String position_code = temp.get("position_code")==null?"":temp.get("position_code");
		if("TP".equals(position_code)){
			map.put("储存区-仓位号", area_code);
		}else{
			map.put("储存区-仓位号", area_code + "-" + position_code);
		}
		
		return map;
	}
	
	/**
	 * 紧急出入库
	 * @date 2017年11月28日
	 * @author 高海涛
	 * @param temp
	 * @return
	 */
	public Map<String,String> setMapToJJCRKLabel(Map<String,String> temp){
		Map<String,String> map = new HashMap<String,String>();
		String demand_dept = temp.get("demand_dept")==null?"":temp.get("demand_dept");
		String demand_person = temp.get("demand_person")==null?"":temp.get("demand_person");
		String create_time = temp.get("create_time")==null?"":temp.get("create_time");
		String urgence_code = temp.get("urgence_code")==null?"":temp.get("urgence_code");
		map.put("订单号", urgence_code);
		map.put("需求部门", demand_dept);
		map.put("责任人", demand_person);
		map.put("创建日期", create_time);
		return map;
	}
}

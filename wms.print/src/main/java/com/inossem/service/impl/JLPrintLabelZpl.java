package com.inossem.service.impl;

import java.util.List;
import java.util.Map;

import com.inossem.core.Constant;
import com.inossem.core.ZplPrint;
import com.inossem.service.PrintService;

/** 
* @author 高海涛 
* @version 2017年10月31日
* ZPL标签打印
*/
public class JLPrintLabelZpl implements PrintService {

	//批次打印
	private boolean chargPrintForJL(Map<String,String> data){
		
		ZplPrint zplPrint = new ZplPrint();
		
		zplPrint.zplInit(data.get("printName"),550);
		zplPrint.setQRcode("50","30","2","8",data.get("matnr"));
		
		int[] xy = new int[] { 240, 30 };

		xy = zplPrint.setLabelValue(xy,0, "批次号", data.get("charg"));
		xy = zplPrint.setLabelValue(xy,0, "采购订单",data.get("ebeln"));
		xy = zplPrint.setLabelValue(xy,0, "合同号", data.get("zhtbh"));
		xy = zplPrint.setLabelValue(xy,0, "入库时间", data.get("inDate"));
		
		xy[0] = 50;
		xy[1] = 220;
		xy = zplPrint.setLabelValue(xy,0, "物料编号", data.get("matnr"));
		xy = zplPrint.setLabelValue(xy,0, "物料描述", data.get("maktx"));
		xy = zplPrint.setLabelValue(xy,0, "供应商", data.get("vname1"));

		String zplString = zplPrint.getZplString();
		System.out.println("ZPL完整代码 : " + zplString);
		
		return zplPrint.createPrintJob(zplString);
	}
	
	//物料打印
	private boolean materielPrintForJL(Map<String,String> data){
		
		ZplPrint zplPrint = new ZplPrint();
		
		zplPrint.zplInit(data.get("printName"),550);
		zplPrint.setQRcode("50","40","2","8",data.get("id"));
		
		int[] xy = new int[] { 240, 50 };

		
		xy = zplPrint.setLabelValue(xy,0, "物料编码",data.get("matnr"));
		xy = zplPrint.setLabelValue(xy,0, "物料描述", data.get("maktx"));
		
		
		xy[0] = 50;
		xy[1] = 230;
		xy = zplPrint.setLabelValue(xy,30, "采购订单", data.get("ebeln"));
		xy = zplPrint.setLabelValue(xy,30, "需求跟踪号", data.get("number"));
		xy = zplPrint.setLabelValue(xy,30, "订单备注", data.get("remarks"));
		
		
		String zplString = zplPrint.getZplString();
		System.out.println("ZPL完整代码 : " + zplString);
		
		return zplPrint.createPrintJob(zplString);
	}
	
	
	//仓位打印
	private boolean positionPrintForJL(Map<String,String> data){
		
		ZplPrint zplPrint = new ZplPrint();
		
		zplPrint.zplInit(data.get("printName"),550);
		zplPrint.setQRcode("50","70","2","8",data.get("id"));
		
		int[] xy = new int[] { 240, 80 };

		xy = zplPrint.setLabelValue(xy,0, "仓位编码", data.get("lgpla"));
		xy = zplPrint.setLabelValue(xy,0, "物料编码",data.get("matnr"));
		xy = zplPrint.setLabelValue(xy,0, "物料描述", data.get("maktx"));
		
		xy[0] = 55;
		xy[1] = 270;
		
		xy = zplPrint.setLabelValue(xy,20, "订单备注", data.get("remarks"));

		String zplString = zplPrint.getZplString();
		System.out.println("ZPL完整代码 : " + zplString);
		
		return zplPrint.createPrintJob(zplString);
	}
		
		
	@SuppressWarnings("unchecked")
	@Override
	public String mdPrint(@SuppressWarnings("rawtypes") List<Map> dataList, String type) {
		int count = 0;
		Boolean flag = false ;
		for(Map<String,String> data : dataList){
			if(type.equals(Constant.TYPE_1)){
				flag = this.chargPrintForJL(data);
				if(flag){
					count ++;
					flag = false;
				}
			}else if(type.equals(Constant.TYPE_2)){
				flag = this.materielPrintForJL(data);
				if(flag){
					count ++;
					flag = false;
				}
			}else if(type.equals(Constant.TYPE_3)){
				flag = this.positionPrintForJL(data);
				if(flag){
					count ++;
					flag = false;
				}
			}
		}
		
		return String.valueOf(count);
	}

}

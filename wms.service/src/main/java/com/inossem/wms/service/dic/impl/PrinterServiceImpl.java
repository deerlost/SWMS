package com.inossem.wms.service.dic.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicPrinterMapper;
import com.inossem.wms.model.dic.DicClassType;
import com.inossem.wms.model.dic.DicPrinter;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.service.dic.IPrinterService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONObject;
@Transactional
@Service("printerService")
public class PrinterServiceImpl implements IPrinterService{
    @Autowired 
    private DicPrinterMapper  dicPrinterMapper;
	
	@Override
	public List<Map<String,Object>> getPrinterList(Map<String, Object> param) {
		return dicPrinterMapper.selectPrinterOnpaging(param);
	}

	@Override
	public boolean saveOrUpdateClassType(JSONObject json) {
		
		DicPrinter dicPrinter=new DicPrinter();
		boolean isAdd=true;
		int i=-1;
		if(json.containsKey("printer_id")) {
			dicPrinter.setPrinterId(UtilObject.getIntegerOrNull(json.get("printer_id")));
			isAdd=false;
		}
		//dicPrinter.setType(UtilObject.getByteOrNull(json.get("type")));
		dicPrinter.setPrinterName(UtilObject.getStringOrEmpty(json.get("printer_name")));
		dicPrinter.setPrinterIp(UtilObject.getStringOrEmpty(json.get("printer_ip")));
		dicPrinter.setIsDelete(EnumBoolean.FALSE.getValue());
		if(isAdd) {
			i=dicPrinterMapper.insertSelective(dicPrinter);
		}else {
			i=dicPrinterMapper.updateByPrimaryKeySelective(dicPrinter);
		}	
		if(i<1) {
			return false;
		}
		return true;
		
	}

	@Override
	public boolean deletePrinterById(Integer printerId) {
		Integer result=-1;
		result=dicPrinterMapper.deletePrinterById(printerId);
		if(result>0) {
			return true;	
		}		 
		 return false;
	}
	

}

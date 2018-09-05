package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicPrinter;

import net.sf.json.JSONObject;

public interface IPrinterService {

	
   List<Map<String,Object>>	getPrinterList(Map<String,Object> param);
   boolean saveOrUpdateClassType(JSONObject json);
   boolean deletePrinterById(Integer printerId);
}

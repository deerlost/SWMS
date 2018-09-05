package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicProductLine;

import net.sf.json.JSONObject;

public interface IProductLineService {

	
   List<DicProductLine>	getDicProductLineList(Map<String,Object> parameter);
   
    boolean  saveOrUpdateProductLine(JSONObject json) throws Exception;
    
    int    deleteProductLine(Integer productLineId);
    
    List<DicProductLine> selectProductInstallationList();
}

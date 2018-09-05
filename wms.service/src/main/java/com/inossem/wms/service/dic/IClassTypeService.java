package com.inossem.wms.service.dic;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.inossem.wms.model.dic.DicClassType;

import net.sf.json.JSONObject;

public interface IClassTypeService {
	List<Map<String,Object>> getDicClassTypeList(Map<String,Object> parameter);

	boolean saveOrUpdateClassType( JSONObject json)  throws ParseException;
	
	int updateIsDeleteByKey(Integer classTypeId);
}

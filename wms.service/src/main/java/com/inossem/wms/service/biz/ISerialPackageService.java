package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;
import com.inossem.wms.model.dic.DicPackClassification;
import com.inossem.wms.model.vo.SerialPackageVo;

import net.sf.json.JSONObject;


public interface ISerialPackageService {

	List<SerialPackageVo> selectSerialPackageList(Map<String,Object> param);
	
	List<DicPackClassification> selectClassList();
	
	int deleteSerialPackageByIds(List<Integer> ids,String userId)throws Exception;
	
	List<Integer> saveSerialPackage(JSONObject json,String userId)throws Exception;
}

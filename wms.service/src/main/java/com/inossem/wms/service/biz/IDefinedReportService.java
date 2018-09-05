package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.vo.BizDefinedReportVo;

import net.sf.json.JSONObject;

public interface IDefinedReportService {
	
	BizDefinedReportVo getDetailById(Integer definedId);
	
	Map<String, Object> getDefinedField(Byte type);
	
	List<BizDefinedReportVo> getDefinedReportList(Map<String,Object> param);
	
	Integer saveData(JSONObject jsonData,String userId)throws Exception;

	List<Map<String, Object>> getDataList(JSONObject jsonData);

	Integer getDataCount(JSONObject jsonData);
	
	Integer deleteById(Integer definedId)throws Exception;
}
